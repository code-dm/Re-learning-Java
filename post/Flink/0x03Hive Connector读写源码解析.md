---
title: Hive Connector读写源码解析
toc: true
categories:
  - Flink
abbrlink: 76537a8
date: 2022-09-14 11:00:00
---
[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/11)
Flink实现Hive中表数据的读写操作首先要获取到每张表的元数据，通过表的元数据信息才能获取到数据文件的存储路径、存储格式、是否分区、分区规则。这样才能从HDFS对应的路径中获取到对应的数据，并使用对应的存储格式来解析文件。
Flink Hive的读写同样使用Connector通用架构新Source（[FLIP-27](https://cwiki.apache.org/confluence/display/FLINK/FLIP-27%3A+Refactor+Source+Interface)）和Sink。
<!-- more -->
# Flink 读取 Hive 配置信息
Flink 支持以批和流两种模式从 Hive 表中读取数据。批读的时候，Flink 会基于执行查询时表的状态进行查询。流读时将持续监控表，并在表中新数据可用时进行增量获取，默认情况下，Flink 将以批模式读取数据。
流读支持消费分区表和非分区表。对于分区表，Flink 会监控新分区的生成，并且在数据可用的情况下增量获取数据。对于非分区表，Flink 将监控文件夹中新文件的生成，并增量地读取新文件。

| 键                                     | 默认值            | 类型	       | 描述                                                                                                                                                                                                                                                                                                                                                           |
|---------------------------------------|----------------|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| streaming-source.enable               | false          | 	Boolean	 | 是否启动流读。注意：请确保每个分区/文件都应该原子地写入，否则读取不到完整的数据。                                                                                                                                                                                                                                                                                                                    |
| streaming-source.partition.include    | all            | 	String   | 	选择读取的分区，可选项为 `all` 和 `latest`，`all` 读取所有分区；`latest` 读取按照 'streaming-source.partition.order' 排序后的最新分区，`latest` 仅在流模式的 Hive 源表作为时态表时有效。默认的选项是 `all`。在开启 'streaming-source.enable' 并设置 'streaming-source.partition.include' 为 'latest' 时，Flink 支持 temporal join 最新的 Hive 分区，同时，用户可以通过配置分区相关的选项来配置分区比较顺序和数据更新时间间隔。                                              |
| streaming-source.monitor-interval     | None           | 	Duration | 	连续监控分区/文件的时间间隔。 注意: 默认情况下，流式读 Hive 的间隔为 '1 min'，但流读 Hive 的 temporal join 的默认时间间隔是 '60 min'，这是因为当前流读 Hive 的 temporal join 实现上有一个框架限制，即每个 TM 都要访问 Hive metastore，这可能会对 metastore 产生压力，这个问题将在未来得到改善。                                                                                                                                                           |
| streaming-source.partition-order      | partition-name | 	String   | 	streaming source 分区排序，支持 create-time， partition-time 和 partition-name。 create-time 比较分区/文件创建时间， 这不是 Hive metastore 中创建分区的时间，而是文件夹/文件在文件系统的修改时间，如果分区文件夹以某种方式更新，比如添加在文件夹里新增了一个文件，它会影响到数据的使用。partition-time 从分区名称中抽取时间进行比较。partition-name 会比较分区名称的字典顺序。对于非分区的表，总是会比较 'create-time'。对于分区表默认值是 'partition-name'。该选项与已经弃用的 'streaming-source.consume-order' 的选项相同 |
| streaming-source.consume-start-offset | None           | 	String   | 	流模式起始消费偏移量。如何解析和比较偏移量取决于你指定的顺序。对于 create-time 和 partition-time，会比较时间戳 (yyyy-[m]m-[d]d [hh:mm:ss])。对于 partition-time，将使用分区时间提取器从分区名字中提取的时间。 对于 partition-name，是字符串类型的分区名称(比如 pt_year=2020/pt_mon=10/pt_day=01)。                                                                                                                                              |
# Flink 读取 Hive 原理
![流程图](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220920/43be348a6c5d477eb991ae1f20019991.png?x-oss-process=style/codedm)

![HiveSource类依赖图](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220914/33c0117958164715aa805ab0e114dba4.png?x-oss-process=style/codedm)
`HiveSource<T>`继承了`AbstractFileSource<T, HiveSourceSplit>`类，`AbstractFileSource`又实现了核心的`Source`接口。
Hive表中是不存储数据的，所有数据存储在HDFS中，所以`HiveSource`继承了`AbstractFileSource`抽象类。
通过[FLIP-27](https://cwiki.apache.org/confluence/display/FLINK/FLIP-27%3A+Refactor+Source+Interface)我们可以了解到`Source`类的作用：
- 一个工厂类
- 用来创建`SplitEnumerator`和`SourceReader`
- 创建用来生成`CheckPoint`的序列化类，以及从状态中恢复`Enumerator`
## HiveSourceBuilder
`org.apache.flink.connectors.hive.HiveSourceBuilder`类主要是通过配置信息构建`HiveSource`类。
`HiveSourceBuilder`类中核心的方法：`buildWithBulkFormat`。
![buildHiveSource](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220915/826d0c38d3ba49739cef6263cdac7a09.png?x-oss-process=style/codedm)
## HiveSource
`HiveSource`构造方法中的参数：
- `Path[] inputPaths`: 
   文件系统中的一个目录或者一个文件，`buildHiveSource`传过来的参数是：`new Path[1]`
- `FileEnumerator.Provider fileEnumerator`: 
   一个可以创建`HiveSourceFileEnumerator`的工厂类
- `FileSplitAssigner.Provider splitAssigner`: 
   创建`FileSplitAssigner`工厂类
- `BulkFormat<T, HiveSourceSplit> readerFormat`: 
   首先了解什么是`BulkFormat`：`BulkFormat`是一个接口，`BulkFormat` 一次读取并解析一批记录。`BulkFormat`的实现包括`ORC Format`、`Parquet Format`、`HiveInputFormat`等。 
   外部的`BulkFormat`类主要充当`reader`的配置持有者和工厂角色(用来创建`reader`的工厂)。`BulkFormat.Reader`是在`BulkFormat#createReader(Configuration, FileSourceSplit)`方法中创建的，然后完成读取操作。如果在流的`checkpoint`执行期间基于`checkpoint`创建`Bulk reader`，那么`reader`是在`BulkFormat#restoreReader(Configuration, FileSourceSplit)`方法中重新创建的。
- `ContinuousEnumerationSettings continuousEnumerationSettings`: 
   流式读取持续监控分区和文件的配置，包括监控的时间间隔。
   如果是批量读取`continuousEnumerationSettings`为空，如果是流式读取`continuousEnumerationSettings`会被`new`出来。
- `int threadNum`: 
   用来限制最大创建监控Hive分区和文件的线程数，在`org.apache.flink.connectors.hive.MRSplitsGetter`类中`Executors.newFixedThreadPool(threadNum);`限制线程池的数量。
   从`table.exec.hive.load-partition-splits.thread-num`中获取的参数。
- `JobConf jobConf`: 
   通过`HiveConf`转成的`JobConf`
- `ObjectPath tablePath`: 
  table/view/function的名称
- `List<String> partitionKeys`: 
   分区键
- `ContinuousPartitionFetcher<Partition, ?> fetcher`: 
   是一个Hive分区获取器，可以根据`previousOffset`获取之后的分区。需要利用`HiveContinuousPartitionFetcherContext-getComparablePartitionValueList`获取所有可比较的分区列表。
- `HiveTableSource.HiveContinuousPartitionFetcherContext<?> fetcherContext`: 
   ![FetcherContext](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220916/4cc600e281a7457fa02c85cf68a797ee.png?x-oss-process=style/codedm)
   从`Hive`的`Meta Store`中获取表的分区的上下文。
`HiveSource`继承`AbstractFileSource`类，`AbstractFileSource-createReader`会创建一个核心类`FileSourceReader`，用来读取分布式文件系统中的文件。
`HiveSource-createEnumerator`会创建核心类`ContinuousHiveSplitEnumerator`(流式读取)或调用父类`createEnumerator`方法创建`StaticFileSplitEnumerator`(批方式读取)类。
## ContinuousHiveSplitEnumerator
![ContinuousHiveSplitEnumerator](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220919/0c6cbc82f92a49f4a49738e68a336683.png?x-oss-process=style/codedm)
分区的发现分为初始化阶段和后续持续监控阶段，`ContinuousHiveSplitEnumerator`的作用是定时发现分布式文件系统中的分区。`ContinuousHiveSplitEnumerator`实现了`SplitEnumerator`类，程序会定时调用该类的`start()`方法用来监控分区。
`start()`方法中`enumeratorContext.callAsync(monitor, this::handleNewSplits, discoveryInterval, discoveryInterval)`方法的入参：
- monitor：回调类，会定期调用该类的`call()`方法
- this::handleNewSplits：传入的是一个函数。用来处理`monitor`发现的新分区
- discoveryInterval：初始化延迟时间
- discoveryInterval：周期延迟时间
### PartitionMonitor#call()方法
![PartitionMonitor](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220919/974ff4cb74214d30b6edf72e8afa8eda.png?x-oss-process=style/codedm)
`call()`方法会获取表的所有分区，并过滤掉`currentReadOffset`位点之后的之前的旧分区。然后根据这些分区循环生成`HiveSourceSplit`，最终返回`NewSplitsAndState`。
`NewSplitsAndState`中存储三个全局变量：
- T offset: 最新的offset
- Collection<List<String>> seenPartitions: 已经处理过的分区offset
- Collection<HiveSourceSplit> newSplits: 监控到的新分区，如果没有监控到则为空
## StaticFileSplitEnumerator
`FileSource`批处理`SplitEnumerator`的具体实现。获取文件系统目录中所有文件并分配给`Reader`。`HiveSource`的批处理使用该类做处理。
## FileSourceReader
```
public FileSourceReader(
            SourceReaderContext readerContext,
            BulkFormat<T, SplitT> readerFormat,
            Configuration config) {
        super(
                () -> new FileSourceSplitReader<>(config, readerFormat),
                new FileSourceRecordEmitter<>(),
                config,
                readerContext);
    }
```
核心作用在构造方法中向父类传入`() -> new FileSourceSplitReader<>(config, readerFormat)`。真正调用读取逻辑的是`FileSourceSplitReader`
## FileSourceSplitReader
`FileSourceSplitReader`类中`fetch`方法调用批量读取方法：
```
@Override
public RecordsWithSplitIds<RecordAndPosition<T>> fetch() throws IOException {
  checkSplitOrStartNext();

  final BulkFormat.RecordIterator<T> nextBatch = currentReader.readBatch();
  return nextBatch == null
          ? finishSplit()
          : FileRecords.forRecords(currentSplitId, nextBatch);
}
```
`BulkFormat`在`HiveSource`构造方法参数中有详细讲解过。

