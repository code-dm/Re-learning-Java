---
title: Docker安装MySQL并开启BinLog
toc: true
categories:
  - Docker
abbrlink: 5c6bc735
date: 2022-07-24 00:00:00
---
> 确保已经安装Docker
> 适用于 MySQL5.7
# 创建配置文件

创建一个配置文件目录，并新建一个``my.cnf``文件，并插入下面内容：
```
#
# The MySQL  Server configuration file.
#
# For explanations see
# http://dev.mysql.com/doc/mysql/en/server-system-variables.html

[mysqld]
pid-file        = /var/run/mysqld/mysqld.pid
socket          = /var/run/mysqld/mysqld.sock
datadir         = /var/lib/mysql
secure-file-priv= NULL
default-time-zone = '+8:00'

#最大链接数
max_connections=1024

#是否对sql语句大小写敏感，1表示不敏感
lower_case_table_names=1
log_bin_trust_function_creators=1
server-id=1000
#启用log-bin
log-bin=mysql-bin

#设置日志格式
binlog-format=Row

#设置binlog清理时间
expire_logs_days=7

# 数据表默认时区
default-time-zone='+08:00'

# Custom config should go here
!includedir /etc/mysql/conf.d/
```
# 执行启动命令

```shell
docker run -p 3308:3306 \
--name mysql-binlog \
-v /Users/wudongming/code/docker/mysql-conf/my.cnf:/etc/mysql/my.cnf \
-e MYSQL_ROOT_PASSWORD=123456 \
-d \
mysql:5.7.34
```
> 如果您本地没有`mysql:5.7.34`这个镜像，启动的时候会自动拉取这个镜像，拉取时间根据您的网速决定。