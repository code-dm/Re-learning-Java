---
title: Docker安装MySQL
toc: true
categories:
  - Docker
abbrlink: 5c6bc735
date: 2022-07-24 00:00:00
---
> 确保已经安装Docker
> 适用于 MySQL5.7

1. 创建一个配置文件目录，并新建一个``my.conf``文件
2. 执行命令
```shell
docker run -p 3306:3306 \
--name mysql8 -v 你创建的目录:/etc/mysql/conf.d \
-e MYSQL_ROOT_PASSWORD=123456 -d 镜像ID
```

docker run \
-p 33261:3306 \
-e MYSQL_ROOT_PASSWORD=123456 \
-d mysql:tag \
--name some-mysql