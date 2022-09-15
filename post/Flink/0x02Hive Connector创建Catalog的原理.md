---
title: Hive Connector创建Catalog的原理
toc: true
categories:
  - Flink
abbrlink: 31388dbe
date: 2022-09-13 11:00:00
---
[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/11)

> 本文基于：Flink-1.15
# 简介
在sql-client中使用Hive Connector之前常规都是需要将Flink和Hive Metastore连接起来，从Hive Metastore中获取Hive的元数据，方便我们直接操作Hive中的一些表。同时我们在sql-client中创建对表也可以持久化在Hive Metastore中。
使用Flink HiveCatalog可以连接到Hive Metastore，这边文章主要来了解HiveCatalog的实现。
<!-- more -->
# HiveCatalog
```sql
CREATE CATALOG myhive WITH (
    'type' = 'hive',
    'default-database' = 'mydatabase',
    'hive-conf-dir' = '/opt/hive-conf'
);
```
使用`CREATE CATALOG`语句Flink Table会根据type使用SPI找到对应的工厂类：`HiveCatalogFactory`。
`HiveCatalogFactory`主要作用是获取相关配置，并创建`HiveCatalog`类。
![类关系图](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220913/7eabfa7845454af0b00653a5c0ebb06e.png?x-oss-process=style/codedm)
此处创建的`Catalog`会被管理在`CatalogManager`类中。
![CatalogManager](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220913/cf0ac703cace4480bd5167d1bd2d51ac.png?x-oss-process=style/codedm)
`HiveCatalog`中的一些方法
- getDatabase、createDatabase、alterDatabase、listDatabase、、、
- getTable、createTable、renameTable、alterTable、dropTable、、、
- partitionExists、createPartition、、、
- createFunction、alterFunction、、、
- alterTableStatistics、alterPartitionStatistics、、、
- 、、、

通过这些方法我们可以增删改查`Hive`中的数据库、表、视图以及函数等等。
# HiveShim
HiveCatalog并不是直接连接Hive Metastore的，而是通过 HiveShim 来连接到Hive Metastore的。
HiveShim 的作用是处理Flink连接 Hive 在不同 Hive 版本版本中 Hive Metastore 不兼容的问题。
# 作业执行流程
## 提交创建Catalog语句
```sql
CREATE CATALOG myhive WITH (
    'type' = 'hive',
    'default-database' = 'mydatabase',
    'hive-conf-dir' = '/opt/hive-conf'
);
```
用户通过 SQL Client 提交CREATE CATALOG SQL请求，Flink 会创建 TableEnvironment， TableEnvironment 会创建 CatalogManager 加载并配置 HiveCatalog 实例。
初始化HiveCatalog会获取Hive的版本。后续会使用到。
## 提交USE语句
```sql
USE CATALOG myhive;
```
CatalogManager 会修改全局currentCatalogName属性为`myhive`。
## SHOW 语句
```sql
SHOW DATABASES;
```
Flink Table 会调用HiveCatalog的listDatabases方法，Hive大版本不会存在差异的接口会使用`IMetaStoreClient client`直接获取getAllDatabases。
## getHiveMetastoreClient
getHiveMetastoreClient在Hive各个大版本初始化存在差异，Flink使用HiveShim来处理不同版本的不同适配代码。
例如：
Hive1.0.0初始化HiveMetastoreClient代码：
```
    @Override
    public IMetaStoreClient getHiveMetastoreClient(HiveConf hiveConf) {
        try {
            return new HiveMetaStoreClient(hiveConf);
        } catch (MetaException ex) {
            throw new CatalogException("Failed to create Hive Metastore client", ex);
        }
    }
```
Hive2.0.0初始化HiveMetastoreClient代码：
```
    @Override
    public IMetaStoreClient getHiveMetastoreClient(HiveConf hiveConf) {
        try {
            Class<?>[] constructorArgTypes = {HiveConf.class};
            Object[] constructorArgs = {hiveConf};
            Method method =
                    RetryingMetaStoreClient.class.getMethod(
                            "getProxy",
                            HiveConf.class,
                            constructorArgTypes.getClass(),
                            constructorArgs.getClass(),
                            String.class);
            // getProxy is a static method
            return (IMetaStoreClient)
                    method.invoke(
                            null,
                            hiveConf,
                            constructorArgTypes,
                            constructorArgs,
                            HiveMetaStoreClient.class.getName());
        } catch (Exception ex) {
            throw new CatalogException("Failed to create Hive Metastore client", ex);
        }
    }
```
Hive3.1.0初始化HiveMetastoreClient代码：
```
    @Override
    public IMetaStoreClient getHiveMetastoreClient(HiveConf hiveConf) {
        try {
            Method method =
                    RetryingMetaStoreClient.class.getMethod(
                            "getProxy", Configuration.class, Boolean.TYPE);
            // getProxy is a static method
            return (IMetaStoreClient) method.invoke(null, hiveConf, true);
        } catch (Exception ex) {
            throw new CatalogException("Failed to create Hive Metastore client", ex);
        }
    }
```