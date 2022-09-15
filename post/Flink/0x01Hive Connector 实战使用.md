---
title: Hive Connector 实战使用
toc: true
categories:
  - Flink
abbrlink: e220ca62
date: 2022-09-07 11:00:00
---
[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/11)
<!-- more -->
> 本文基于：Flink-1.15
# 准备工作
### 下载 Flink 安装包
```shell
wget https://dlcdn.apache.org/flink/flink-1.15.2/flink-1.15.2-bin-scala_2.12.tgz --no-check-certificate
```
### 下载 Hive 依赖包
选择一个下载并放在 Flink 解压包的/lib/ 目录中。

| Metastore version | Maven dependency               | SQL Client JAR                                                                                                                                              |
|-------------------|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.0.0 - 1.2.2     | flink-sql-connector-hive-1.2.2 | [Download](https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-1.2.2_2.12/1.15.2/flink-sql-connector-hive-1.2.2_2.12-1.15.2.jar) |
| 2.0.0 - 2.2.0     | flink-sql-connector-hive-2.2.0 | [Download](https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-2.2.0_2.12/1.15.2/flink-sql-connector-hive-2.2.0_2.12-1.15.2.jar) |
| 2.3.0 - 2.3.6     | flink-sql-connector-hive-2.3.6 | [Download](https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-2.3.6_2.12/1.15.2/flink-sql-connector-hive-2.3.6_2.12-1.15.2.jar) |
| 3.0.0 - 3.1.2     | flink-sql-connector-hive-3.1.2 | [Download](https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-3.1.2_2.12/1.15.2/flink-sql-connector-hive-3.1.2_2.12-1.15.2.jar) |
### Hadoop 依赖
```shell
export HADOOP_CLASSPATH=`hadoop classpath`
```
### 配置 Kerberos
如果 Hive 开启的 Kerberos 则进行配置，如果没有则跳过。
在 `conf/flink-conf.yam` 配置文件中增加以下配置。
```
security.kerberos.login.use-ticket-cache: true
security.kerberos.login.keytab: /keytabs/hive.service.keytab
security.kerberos.login.principal: hive
```
### 启动一个 Flin Standalone 环境
为了可以远程访问 Flink Web界面，你可以将 `conf/flink-conf.yam` 中所有 `localhost` 全部替换成 `0.0.0.0`。
增加指定端口配置：
```
rest.port: 8081
```
启动集群：
```shell
bin/start-cluster.sh
```
如果集群启动成功，通过浏览器可以访问Flink Web界面。
![Flink Web界面](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220907/f5815234be584c95a22b20cd288e5841.png?x-oss-process=style/codedm)
注意`Available Task Slots`必须 >= 1，如果为 `0`，则启动有异常。
### 连接 Hive Catalog
```shell
bin/sql-client.sh
```
```sql
CREATE CATALOG myhive WITH (
    'type' = 'hive',
    'default-database' = 'yyq',
    'hive-conf-dir' = '/etc/hive/conf'
);
```
`CREATE CATALOG`的一些详细配置：
输入 `show databases;` 可以显示 Hive 中所有的数据库。
也可以写个 `SELECT`，sql-client会将作业提交到 Standalone 集群中，并返回查询结果。

| 参数               | 必选  | 默认值     | 类型     | 描述                                                                                                                                       |
|------------------|-----|---------|--------|------------------------------------------------------------------------------------------------------------------------------------------|
| type             | 是   | (无)     | String | Catalog 的类型。 创建 HiveCatalog 时，该参数必须设置为'hive'。                                                                                            |
| name             | 是   | (无)     | String | Catalog 的名字。仅在使用 YAML file 时需要指定。                                                                                                        |
| hive-conf-dir    | 否   | (无)     | String | 指向包含 hive-site.xml 目录的 URI。 该 URI 必须是 Hadoop 文件系统所支持的类型。 如果指定一个相对 URI，即不包含 scheme，则默认为本地文件系统。如果该参数没有指定，我们会在 class path 下查找hive-site.xml。 |
| default-database | 否   | default | String | 当一个catalog被设为当前catalog时，所使用的默认当前database。                                                                                                |
| hive-version     | 否   | (无)     | String | HiveCatalog 能够自动检测使用的 Hive 版本。我们建议不要手动设置 Hive 版本，除非自动检测机制失败。                                                                             |
| hadoop-conf-dir  | 否   | (无)     | String | Hadoop 配置文件目录的路径。目前仅支持本地文件系统路径。我们推荐使用 HADOOP_CONF_DIR 环境变量来指定 Hadoop 配置。因此仅在环境变量不满足您的需求时再考虑使用该参数，例如当您希望为每个 HiveCatalog 单独设置 Hadoop 配置时。  |
### 从MySQL中同步数据到Hive

