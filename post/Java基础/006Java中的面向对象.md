---
title: Java中的面向对象OOP
toc: true
categories:
  - Java基础
cover: >-
  https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220809/368fdfeac11741259485eb88646996a4.png?x-oss-process=style/codedm
abbrlink: fd4a56f1
date: 2022-08-08 23:02:00
---

[本篇文章对应的Issues，Issues代表我文章创作的过程。(点击我)](https://github.com/Code-dm/Re-learning-Java/issues/7)
面向对象OOP（Object-oriented programming）
<!-- more -->
# 面向过程 Process-oriented programming, POP
## 开个网店
本章节很重要，OOP是Java编程的核心思想，所以本章节我会尽力写的更加详细，用更多的例子来讲解。
在讲Java的面向对象之前我们想讲讲为什么会有面向对象这个东西。
在面向对象编程思想之前有一种面向过程(Process-oriented programming, POP)的编程思想，C语言其实就是面向过程的，面向过程的意思是我们按照事件发展的流程来写代码，每行代码对应事件发展的每个节点，直到事件结束，代码也就结束了。
伴随着软件的发展，需求越来越复杂，代码也来越庞大，事件发展的分支越来越多，面向过程的弊端就凸显了，因为根本就没办法维护，修改一个小需求需要把整个过程走一遍。
我们理解OOP的最好办法就是来看看面向过程在复杂场景为什么会这么吃力。
文中使用案例来自：https://dins.site/coding-advanced-oop-motivation-chs/
假设你是一个水果商人，你看到了互联网的发展给零售业带来的冲击和商机，于是你准备自己办一个网站，线上卖水果。由于你自己懂编程，所以你想自己写一个后台，把web端交给另一个人去做。这样即节省成本又有乐趣。于是你们两个约定好，web端给用户提供各种选择，其结果用二维数组的方式传给后台，后台根据文件计算水果价格收据，再返回给web端。
这个故事发生在90年代，所以不需要考虑其他复杂的因素，把程序做出来就可以了。为了方便我们使用控制台的方式，通过控制台代替文本来传输各种选择给后端，后端计算价格在输出给控制台。
```java
/**
 * 面向过过程的程序设计
 *
 * @author Codedm
 */
public class PopDemo {

    public static void main(String[] args) {
        // 定义一个二维数组 存储水果序号、水果名称、水果单价
        String[][] fruitData = {
                {"苹果", "2"},
                {"橘子", "5"},
                {"香蕉", "10"},
                {"芒果", "13"},
                {"葡萄", "15"}
        };

        System.out.println("请输入您需要购买水果的序号：");
        // 使用for循环打印各个水果名称和序号
        for (int i = 0; i < fruitData.length; i++) {
            System.out.println("序号：" + i + "，" + fruitData[i][0]);
        }

        Scanner sc = new Scanner(System.in);
        int fruitNumber = sc.nextInt(); 
        System.out.println("您选择的水果是：" + fruitData[fruitNumber][0] + "，它的单价是：" + fruitData[fruitNumber][1]);
        System.out.println("请输入购买的重量：");
        
        // 此场景仅仅为了演示面向过程的编码风格，请忽略使用int类型来接收重量和价格。
        int weight = sc.nextInt();
        int totalPrice = Integer.parseInt(fruitData[fruitNumber][1]) * weight;
        System.out.println("总价为：" + totalPrice);

    }
}
```
## 吸引更多的顾客
大家思考下现在我们是简单的计算价格的逻辑，渐渐的有更多的人来在线购买，同时为了吸引更多的用户，我们想发放优惠券。优惠券的逻辑还要增加在这个现有的逻辑里面。
再比如还会有满减，配送费等等其他复杂的逻辑。
## FOP为什么会失败？
上述代码可以看出POP是紧耦合的，需求变更必然需要修改原来的代码。这点如果在代码不复杂的请看下看不出来，但是随着代码量越来越多问题就会越来越大。
企业级的代码肯定不是几十行或者几百行可以完成的。OOP的出现其实就是从设计上来解决这种复杂代码的变更带来的维护问题。
# 面向对象OOP
## 什么是类和对象
在面向对象的编程中，类是来用创建对象的图纸、蓝图。通常来说一个类可以表示为一个人、一个地方或者一个事物，是一种抽象。
比如说盖一个房子，房子的图纸（这张纸）是Java中的.java 文件，图纸上房子的架构就是类，根据这个图纸盖出来的房子是对象。对象是类的一个具象化的实体。我们可以拿这个图纸盖很多个房子，所以类只有一个，一个类可以构造出多个对象。
## 在Java中创建一个类
