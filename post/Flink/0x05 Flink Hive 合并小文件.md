---
title: Flink Hive 合并小文件原理
toc: true
categories:
- Flink
date: 2022-10-11 17:00:00
---
# 服务版本
- Flink 1.5.2
- MySQL 5.7.34 
- Flink CDC 2.2.1
- Hive 3.0.0
# 同步场景
我们的订单系统数据存储在事务型数据库MySQL中，订单系统中有一个核心的表`order`表。我们需要将`order`表中的数据实时同步到Hive的数据仓库中。
Hive中的`order`表是根据订单时间的分区表。`order`表中`order_date`字段是下单时间，下单时间业务方设计是数据插入时间，由数据库`now()`函数生成。由于业务系统会有删除数据的情况，所以在Hive中需要能够有一个字段标识数据的状态。
# 同步配置
## Flink 集群安装
Flink和Hive配置见文章：[Flink和Hive配置]()
## Flink CDC 依赖安装
本次测试我们使用`SQL Client`进行SQL任务的提交。
下载 [flink-sql-connector-mysql-cdc-2.3-SNAPSHOT.jar](https://repo1.maven.org/maven2/com/ververica/flink-sql-connector-mysql-cdc/2.2.1/flink-sql-connector-mysql-cdc-2.2.1.jar) 到 <FLINK_HOME>/lib/ 目录下。
下载 [mysql-connector-java-8.0.28.jar](https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar) 到 <FLINK_HOME>/lib/ 目录下。 
> 注意：如果您已经启动Flink集群，添加上述依赖后需要对集群进行重启。
## MySQL配置
MySQL需要开启BinLog日志。
安装见：[Docker安装MySQL]()
建库建表：
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE `db_order`;
USE db_order;
-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `order_id` int(10) NOT NULL AUTO_INCREMENT,
  `order_date` datetime NOT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `price` decimal(10,5) DEFAULT NULL,
  `product_id` int(10) DEFAULT NULL,
  `order_status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `db_order`.`t_order` (`order_id`, `order_date`, `customer_name`, `price`, `product_id`, `order_status`) VALUES (1, '2022-10-12 15:14:36', '客户1', 129.14200, 10001, b'0');
INSERT INTO `db_order`.`t_order` (`order_id`, `order_date`, `customer_name`, `price`, `product_id`, `order_status`) VALUES (2, '2022-10-12 15:14:36', '客户2', 130.14200, 10002, b'0');
INSERT INTO `db_order`.`t_order` (`order_id`, `order_date`, `customer_name`, `price`, `product_id`, `order_status`) VALUES (3, '2022-10-12 15:14:36', '客户3', 130.14200, 10003, b'0');
INSERT INTO `db_order`.`t_order` (`order_id`, `order_date`, `customer_name`, `price`, `product_id`, `order_status`) VALUES (5, '2022-10-13 15:14:36', '客户5', 130.14200, 10003, b'0');

DELETE FROM `db_order`.`t_order` where order_id = 5;

UPDATE `db_order`.`t_order` SET `order_date` = '2022-10-12 15:14:36', `customer_name` = '客户22', `price` = 130.14200, `product_id` = 10002, `order_status` = b'0' WHERE `order_id` = 2;

SET FOREIGN_KEY_CHECKS = 1;
```
## Hive 配置
Hive中创建分区表，`order_date`订单时间，使用订单时间的年、月、日作为三个分区字段。表的存储格式使用ORC格式。
使用Hive的sql-client执行建表语句，或者使用Flink的sql-client，如果使用flink sql-client，需要切换成Hive方言`SET 'table.sql-dialect' = 'hive';`
> **_NOTE:_** 
> - 为了使用 Hive 方言, 你必须首先添加和 Hive 相关的依赖. 请参考 [Hive dependencies](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/connectors/table/hive/overview/#dependencies) 如何添加这些依赖。
> - 从 Flink 1.15版本开始，如果需要使用 Hive 方言的话，请首先将 FLINK_HOME/opt 下面的 flink-table-planner_2.12 jar 包放到 FLINK_HOME/lib 下，并将 FLINK_HOME/lib 下的 flink-table-planner-loader jar 包移出 FLINK_HOME/lib 目录。否则将抛出 ValidationException。具体原因请参考 [FLINK-25128](https://issues.apache.org/jira/browse/FLINK-25128)。
> - 请确保当前的 Catalog 是 [HiveCatalog](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/connectors/table/hive/hive_catalog/). 否则, 将使用 Flink 的默认方言。 在启动了 HiveServer2 endpoint 的 SQL Gateway，默认当前的 Catalog 就是 HiveCatalog。
> - 为了实现更好的语法和语义的兼容，强烈建议首先加载 [HiveModule](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/connectors/table/hive/hive_functions/#use-hive-built-in-functions-via-hivemodule) 并将其放在 Module 列表的首位，以便在函数解析时优先使用 Hive 内置函数。 请参考文档 [here](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/dev/table/modules/#how-to-load-unload-use-and-list-modules) 来将 HiveModule 放在 Module 列表的首。 在启动了 HiveServer2 endpoint 的 SQL Gateway，HiveModule 已经被加载进来了。
> - Hive 方言只支持 db.table 这种两级的标识符，不支持带有 Catalog 名字的标识符。
> - 虽然所有 Hive 版本支持相同的语法，但是一些特定的功能是否可用仍取决于你使用的 [Hive 版本](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/connectors/table/hive/overview/#%e6%94%af%e6%8c%81%e7%9a%84hive%e7%89%88%e6%9c%ac)。例如，更新数据库位置 只在 Hive-2.4.0 或更高版本支持。
> - Hive 方言主要是在批模式下使用的，某些 Hive 的语法([Sort/Cluster/Distributed BY](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/dev/table/hive-compatibility/hive-dialect/queries/sort-cluster-distribute-by/), [Transform](https://nightlies.apache.org/flink/flink-docs-release-1.16/zh/docs/dev/table/hive-compatibility/hive-dialect/queries/transform/), 等)还没有在流模式下支持。
```sql
CREATE TABLE IF NOT EXISTS order_orc
(
    order_id int,
    order_date Timestamp,
    customer_name string,
    price decimal(10, 5),
    product_id int,
    order_status BOOLEAN,
    op string,
    `kafka_timestamp` TIMESTAMP
) COMMENT 'order_orc' PARTITIONED BY
(
    `order_date_dt` string,
    `order_date_hr` string
)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    with serdeproperties('serialization.null.format' = '')
    STORED AS ORC
    TBLPROPERTIES
(
    'partition.time-extractor.timestamp-pattern'='$order_date_dt $order_date_hr:00:00',
    'sink.partition-commit.trigger'='partition-time',
    'sink.partition-commit.delay'='3 m',
    'sink.partition-commit.watermark-time-zone'='Asia/Shanghai',
    'sink.partition-commit.policy.kind'='metastore,success-file'
);
```
# 开始同步
> 同步之前请确保`SQL Client`已经注册Hive CATALOG，Flink CDC可以正常访问MySQL。
```sql
-- 设置checkpoint间隔时间为10s，生产环境建议2-3分钟 
SET 'execution.checkpointing.interval' = '10s';
    
-- 创建连接MySQL BinLog的Flink表
CREATE TABLE f_order (
     order_id INT,
     order_date TIMESTAMP(0),
     customer_name STRING,
     price DECIMAL(10, 5),
     product_id INT,
     order_status BOOLEAN,
     PRIMARY KEY(order_id) NOT ENFORCED
     ) WITH (
     'connector' = 'mysql-cdc',
     'hostname' = '192.168.72.233',
     'port' = '3308',
     'username' = 'root',
     'password' = '123456',
     'database-name' = 'db_order',
     'table-name' = 't_order');
```
测试Flink CDC能不能正常获取MySQL的BinLog。
```sql
select * from f_order;
-- 按 q 退出查询
```
如果能够正常获取则显示下图，最新数据的获取刷新是根据`SET 'execution.checkpointing.interval' = '10s';`的配置间隔显示。
![获取MySQL BinLog数据](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221012/61f7f79529004bb7809e8bc96961c8b0.png?x-oss-process=style/codedm)

将数据MySQL BinLog数据使用canal格式写入Kafka中
```sql
-- 创建Kafka连接表
CREATE TABLE k_order_write_canal(
    order_id INT,
    order_date TIMESTAMP(0),
    customer_name STRING,
    price DECIMAL(10, 5),
    product_id INT,
    order_status BOOLEAN,
    op STRING METADATA FROM 'op' VIRTUAL,
    PRIMARY KEY(order_id) NOT ENFORCED
) WITH (
    'connector' = 'kafka',
    'topic' = 'flink-cdc-kafka-order',
    'properties.bootstrap.servers' = 'dn90222:6667,dn90224:6667,dn90225:6667,dn90226:6667',
    'properties.group.id' = 'flink-cdc-kafka-group',
    'value.format' = 'canal-json',
    
    'properties.security.protocol' = 'SASL_PLAINTEXT',
    'properties.sasl.mechanism' = 'GSSAPI',
    'properties.sasl.kerberos.service.name' = 'kafka',
    
    'properties.allow.auto.create.topics' = 'true'
    );
-- 将 MySQL BinLog数据写入 Kafka中
insert into k_order_write select * from f_order;
```
将数据从Kafka中同步到Hive
```sql
-- 创建Kafka读表
CREATE TABLE k_order_read(
    data ARRAY<ROW<
        order_id INT,
        order_date TIMESTAMP(0),
        customer_name STRING,
        price DECIMAL(10, 5),
        product_id INT,
        order_status BOOLEAN>>,
    order_id as data[1].order_id,
    order_date as data[1].order_date,
    customer_name as data[1].customer_name,
    price as data[1].price,
    product_id as data[1].product_id,
    order_status as data[1].order_status,
    `type` STRING,
    `timestamp` TIMESTAMP(3) METADATA FROM 'timestamp',
    WATERMARK FOR order_date AS order_date - INTERVAL '5' SECOND
) WITH (
      'connector' = 'kafka',
      'topic' = 'flink-cdc-kafka-order',
      'properties.bootstrap.servers' = 'dn90222:6667,dn90224:6667,dn90225:6667,dn90226:6667',
      'properties.group.id' = 'flink-cdc-kafka-group',
      'value.format' = 'json',

      'properties.security.protocol' = 'SASL_PLAINTEXT',
      'properties.sasl.mechanism' = 'GSSAPI',
      'properties.sasl.kerberos.service.name' = 'kafka',

      'properties.allow.auto.create.topics' = 'true'
      );
-- 同步前请确保Hive的Catalog已经注册
INSERT INTO myhive.yyq.order_orc select order_id, order_date, customer_name, price, product_id, order_status, type, `timestamp`, DATE_FORMAT(order_date, 'yyyy-MM-dd'), DATE_FORMAT(order_date, 'HH') from default_catalog.default_database.k_order_read;
```
在Hive中是历史拉链表，所有的增删改都有记录，通过下面SQL可以过滤掉删除的、旧的数据。
![拉链表数据](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20221014/378863e857144c7b90f466941cd0363c.png?x-oss-process=style/codedm)

```sql
select * from (select order_id, max(kafka_timestamp) as tp from order_orc group by order_id) t 
 join order_orc oc on t.order_id = oc.order_id and t.tp = oc.kafka_timestamp where oc.op != 'DELETE';
```