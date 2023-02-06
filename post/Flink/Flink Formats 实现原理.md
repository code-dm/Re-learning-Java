---
title: Flink Formats 使用以及原理
toc: true
categories:
  - Flink
abbrlink: 3ae81791
date: 2022-07-24 00:00:00
---
Flink 版本1.15.2

案例：

```sql
CREATE TABLE source_table
(
    column_name1 INT,
    column_name2 STRING
) WITH (
      'connector' = 'filesystem',
      'path' = 'file:///Users/wudongming/Documents/datatom/code/flink/flink-examples/flink-examples-table/src/test/resources/test.json',
      'format' = 'json'
      )
```

配置中 ``'format' = 'json'``
，format的配置不仅可以是JSON，还可以是CSV，等等其他配置：[链接](https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/connectors/table/formats/overview/)
![Flink Formats](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221213/0780f016498e41329aa6cbcf33f2e7eb.png?x-oss-process=style/codedm)

如何实现不同根据配置加载不同format：
样例代码

```java

package org.apache.flink.table.examples.java.file;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FileDynamicTableTestCase {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(
                configuration);
        StreamTableEnvironment envT = StreamTableEnvironment.create(env);
        env.setParallelism(1);

        String sourceSQL = "CREATE TABLE source_table (\n"
                + "  column_name1 INT,\n"
                + "  column_name2 STRING\n"
                + ") WITH (\n"
                + "  'connector' = 'filesystem',\n"
                + "  'path' = 'file:///Users/wudongming/Documents/datatom/code/flink/flink-examples/flink-examples-table/src/test/resources/test.json',\n"
                + "  'format' = 'json'\n"
                + ")";
        String printSQL = "CREATE TABLE print_table WITH ('connector' = 'print')\n"
                + "LIKE source_table (EXCLUDING ALL)";

        String etlSQL = "insert into print_table select * from source_table";

        envT.executeSql(sourceSQL);
        envT.executeSql(printSQL);
        envT.executeSql(etlSQL);
    }
}
```

``INSERT INTO`` 之后根据Flink Table的逻辑会找到Source表和Sink表，程序会根据Source表中``'connector' = 'filesystem'``
配置加载``org.apache.flink.connector.file.table.FileSystemTableFactory``类
``FileSystemTableFactory``
文件链接器的工厂类，我们这段逻辑文件连接器是Source，所以会调用``FileSystemTableFactory#createDynamicTableSource``。
``createDynamicTableSource``方法会返回构建好的``org.apache.flink.connector.file.table.FileSystemTableSource``类，
``FileSystemTableSource``构造方法中有两个重要的参数``bulkReaderFormat``和``deserializationFormat``
``bulkReaderFormat``：是批量读取文件时的格式化类，通常用于ORC、AVRO、CSV、Parquet文件的格式化。
``deserializationFormat``用于按行读取的格式化类，通常用于JSON、CSV、AVRO、Maxwell、PggJson等等。
``createDynamicTableSource`` 构建 ``FileSystemTableSource``完成之后，Flink Table会调用``FileSystemTableSource``
类的``getScanRuntimeProvider``方法来初始化扫描数据的类。
``getScanRuntimeProvider``方法会先处理分区、元数据、处理字段投影，然后再处理``bulkReaderFormat``和``deserializationFormat``
先了解 DecodingFormat、ProjectableDecodingFormat、BulkDecodingFormat接口的关系。
![DecodingFormat以及他的子类关系](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221214/1388f9a738e44f04bc9db89620909f03.png?x-oss-process=style/codedm)
flink-table/flink-table-common模块下``org.apache.flink.table.connector.format.Format``是所有格式化接口的父类，最底层的连接器数据格式化接口。
也是``DecodingFormat``和``EncodingFormat``接口的父类。
``DecodingFormat``
是负责读取格式化的接口，该接口中抽象方法``createRuntimeDecoder(DynamicTableSource.Context context, DataType physicalDataType)``
，``physicalDataType``建表时的字段以及字段类型。这个 ``DataType`` 通常是从表的 ``ResolvedSchema``
派生出来的，并且不包括分区、元数据和其他辅助列。``physicalDataType``
应该准确描述完整的序列化记录。换句话说：对于序列化记录中的每个字段，在 ``physicalDataType``
的相同位置都有一个相应的字段。有些实现可能允许用户省略字段，但这取决于数据格式的特点。例如，CSV格式化的实现可能允许用户为每行10列的前5列定义结构。
如果格式支持推测（projections）：可以排除一些不字段，让这些字段不被解析，并且可以在生成的``RowData``
中重新排序字段，那么他应该实现``ProjectableDecodingFormat``接口。
比如``DebeziumJsonDecodingFormat``实现类中，Debezium产生的数据格式是
![DebeziumJson原始数据格式](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221214/707b242754ff4d60a76a3a5a12f42d52.png?x-oss-process=style/codedm)
原始的DebeziumJson数据字段很多，有可能部分字段我们用不到，``DebeziumJsonDecodingFormat``
实现的是``ProjectableDecodingFormat``
接口，则在读取的时候就可以排除一部分字段。``ProjectableDecodingFormat#createRuntimeDecoder``
相比较父类的``createRuntimeDecoder``多了``int[][] projections``参数，传入的是需要的字段，其他字段非必须字段则不会被解析。
![Projection字段](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221214/4b7c487b9cb24b39ab0ceeb79acba08d.png?x-oss-process=style/codedm)







