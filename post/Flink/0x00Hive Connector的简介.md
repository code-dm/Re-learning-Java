---
title: Hive Connector 简介
toc: true
categories:
- Flink
date: 2022-09-07 11:00:00
---
[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/11)
> 本文基于：Flink-1.15
# 简介什么是Hive
如果是了解过Hive以及Flink，这些简介请直接跳过。
## Apache Hive官网的描述
> The Apache Hive ™ data warehouse software facilitates reading, writing, and managing large datasets residing in distributed storage using SQL. Structure can be projected onto data already in storage. A command line tool and JDBC driver are provided to connect users to Hive.

Apache Hive是数据仓库的一种工具，用来读、写和管理分布式存储（例如：HDFS）中的大型的数据集。
他可以把结构映射到存储的文件上。可以通过命令行工具和JDBC驱动来连接Hive。
数据仓库收集企业中各个业务系统的数据进行集中化管理，Apache Hive可以作为数据仓库管理的一种工具。同时可以知道Hive本身是不存储数据的，他管理的是分布式存储系统中文件和结构的映射关系。
例如：HDFS上有一个CSV文件，这个CSV文件使用 `,` 号进行分割
```
a1,a2,a3
b1,b2,b3
c1,c2,c3
```
Apache Hive可以对这个CSV文件进行格式化，定义它的结构，例如定义分隔符、每一列的名称。定义完成后就可以使用SQL语句进行查询。这个过程可以看做是结构化的过程。
# 简介什么是Apache Flink
相对于Apache Hive来说 Apache Flink则是Apache Hive的一个执行计算引擎。常见的Hive计算引擎有MapReduce、Spark，Flink也可以作为Hive的计算引擎。
## Apache Flink官网的描述
> Apache Flink is a framework and distributed processing engine for stateful computations over unbounded and bounded data streams. Flink has been designed to run in all common cluster environments, perform computations at in-memory speed and at any scale.

Apache Flink是对无界和有界数据流进行计算的一个框架和分布式处理的引擎。Flink可以以内存中的运算速度和任何的规模在常见的集群中运行。

# Hive Connector的简介
存储、计算、管理都是数据仓库生态中很重要的一部分。Hive有作为数据仓库生态中的核心，所以Flink针对Hive做了很多适配工作：
- 适配 Hive 的 MetaStore
  利用了 Hive 的 MetaStore 作为持久化的 Catalog，用户可通过HiveCatalog将不同会话中的 Flink 元数据存储到 Hive Metastore 中。 例如，用户可以使用HiveCatalog将其 Kafka 表或 Elasticsearch 表存储在 Hive Metastore 中，并后续在 SQL 查询中重新使用它们。
- 适配Hive方言，可以在Flink SQL-Client或者Flink SQL-Gateway中使用Hive的方言。
- 适配Hive读写
  HiveCatalog的设计提供了与 Hive 良好的兼容性，用户可以"开箱即用"的访问其已有的 Hive 数仓。 您不需要修改现有的 Hive Metastore，也不需要更改表的数据位置或分区。
- 适配Hive的函数
- 等等
