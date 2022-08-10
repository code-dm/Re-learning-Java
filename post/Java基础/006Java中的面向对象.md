---
title: Java中的面向对象OOP
toc: true
categories:
  - Java基础
date: 2022-08-08 23:02:00
cover: https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220809/368fdfeac11741259485eb88646996a4.png?x-oss-process=style/codedm
---

[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/7)
面向对象OOP（Object-oriented programming）
<!-- more -->

在讲Java的面向对象之前我们想讲讲为什么会有面向对象这个东西。
在面向对象编程思想之前有一种面向过程(function-oriented programming, FOP)的编程思想，C语言其实就是面向过程的，面向过程的意思是我们按照事件发展的流程来写代码，每行代码对应事件发展的每个节点，直到事件结束，代码也就结束了。
伴随着软件的发展，需求越来越复杂，代码也来越庞大，事件发展的分支越来越多，面向过程的弊端就凸显了，因为根本就没办法维护，修改一个小需求需要把整个过程走一遍。
我们理解OOP的最好办法就是来看看面向过程在复杂场景为什么会这么吃力。
文中使用案例来自：https://dins.site/coding-advanced-oop-motivation-chs/
假设你是一个水果商人，你看到了互联网的发展给零售业带来的冲击和商机，于是你准备自己办一个网站，线上卖水果。由于你自己懂编程，所以你想自己写一个后台，把web端交给另一个人去做。这样即节省成本又有乐趣。于是你们两个约定好，web端给用户提供各种选择，其结果用文本文件的方式传给后台，后台根据文件计算水果价格收据，再返回给web端。
这个故事发生在90年代，所以不需要考虑其他复杂的因素，把程序做出来就可以了。为了方便我们使用控制台的方式，通过控制台代替文本来传输各种选择给后端，后端计算价格在输出给控制台。
