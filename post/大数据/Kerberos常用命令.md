---
title: Kerberos常用命令
toc: true
categories:
  - 大数据
abbrlink: 12990ea5
date: 2022-08-17 00:00:00
---

通过keytab文件查找Principal
```shell
klist -kte /etc/security/keytabs/flink.keytab
```
命令行kerberos认证：
```shell
kinit -kt /etc/security/keytabs/flink_platform.service.keytab yarn/dn90210@DDP.COM
```

./bin/yarn-session.sh -d -jm 1024m -tm 2048 -s 1

bin/sql-gateway.sh start-foreground -D sql-gateway.endpoint.type=rest -D sql-gateway.endpoint.rest.address=localhost