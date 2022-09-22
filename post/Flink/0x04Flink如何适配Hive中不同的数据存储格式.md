---
title: Flink如何适配Hive中不同的数据存储格式
toc: true
categories:
- Flink
date: 2022-09-21 11:00:00
---
[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/11)
<!-- more --> 
# Hive支持的数据存储格式
- TEXT 格式 (行式存储)
  TEXT FILE 是 Hive 默认文件存储方式，存储方式为行存储，数据不做压缩，磁盘占用大，数据解析使用资源多，数据不支持分片。
- ORC格式 (列式存储)
  ORC文件格式可以提供一种高效的方法来存储Hive数据，运用ORC可以提高Hive的读、写以及处理数据的性能。
- Parquet格式 (列式存储)
  Parquet 是面向分析型业务的列式存储格式，是一个面向列的二进制文件格式，所以是不可以直接读取的，文件中包括该文件的数据和元数据，因此Parquet格式文件是自解析的。Parquet对于大型查询的类型是高效的。对于扫描特定表格中的特定列的查询，Parquet特别有用。Parquet一般使用Snappy、Gzip压缩，默认是Snappy。


