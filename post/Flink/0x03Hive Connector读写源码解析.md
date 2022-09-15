---
title: Hive Connector读写源码解析
toc: true
categories:
  - Flink
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
