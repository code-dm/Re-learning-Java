---
title: Flink CheckPoint 使用以及原理
toc: true
categories:
- Docker
date: 2022-07-24 00:00:00
---

# CheckPoint 使用场景

比如我们现在使用Flink CDC来获取MySQL的变更日志并实时计算订单总金额。Flink程序时24小时不停止的运行的，运行了几天之后程序出错了。如果没有CheckPoint程序是不是需要从几天前开始运行。
如果你开启了CheckPoint只需要从上一次成功的CheckPoint位置开始执行。CheckPoint设计的作用是为了Flink程序容错。

# 什么是CheckPoint

Flink基于Chandy-Lamport算法（分布式快照算法，[相关论文](http://lamport.azurewebsites.net/pubs/chandy.pdf)）技术提供了CheckPoint容错机制，分布式快照可以将同一时间点Task/Operate的状态数据做全局统一快照处理。
这个状态数据可以存储在内存、HDFS、S3等等

# 