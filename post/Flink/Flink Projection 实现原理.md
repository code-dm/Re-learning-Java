---
title: Flink Projection 使用以及原理
toc: true
categories:
  - Flink
abbrlink: a4786e67
date: 2022-07-24 00:00:00
---
建表语句：

```sql
CREATE TABLE t
(
    i INT,
    r ROW < d DOUBLE,
    b BOOLEAN>,
    s STRING
);
```

查询语句

```sql
 SELECT s, r.d
 FROM t;
```

上面的例子中建表字段是
