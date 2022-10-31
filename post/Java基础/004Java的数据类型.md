---
title: Java 的数据类型
toc: true
excerpt: 本篇文章会详细讲解编程语言中的变量，以及Java中的int、double等。
categories:
  - Java基础
cover: >-
  https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/c1a6452d9c8249298a9d7b1b38c27e37.png?x-oss-process=style/codedm
abbrlink: f42f2ee6
date: 2022-07-28 00:00:00
---
[本篇文章对应的Issues，Issues代表我文章创作的过程。(点击我)，Issues代表我文章创作的过程。(点击我)](https://github.com/Code-dm/Re-learning-Java/issues/3)
# 什么是变量
## 变量的定义
我们由一个停车场示例来讲解什么是变量。
在C镇有很多停车场，小区停车场和公共停车场，停车场内根据不同大小的车辆划分了不同的停车位（大中小）。车辆停放必须根据车子的尺寸停放，先定义停车场的规则：
- 小区内的停车场只能停放该小区的车辆
- 小区外的公共停车场允许多个小区停放
- 车辆必须根据车子的尺寸严格停放到对应的车位中

![示例图](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/368c92d02b1146a5898733712f49da1b.png?x-oss-process=style/codedm)

将示例中各个角色和我们今天的变量对应<br>
停车场 -> 变量 </br>
汽车 -> 变量中的值</br>
公共停车场 —> 成员变量</br>
小区内停车场 -> 局部变量</br>
不同尺寸停车位 -> Java中数据类型<br>
根据代码再来看看什么是变量：<br>
```java
public class Variable {

    public static void main(String[] args) {
        
        int a;             // 定义一个int类型的变量 a
        int x, y;          // 定义两个int类型的变量 x 和 y
        int c = 1, d = 2;  // 定义变量 c 赋值 1 和变量 d 赋值 2
        a = 0;             // 给 a 赋值 0
        a = 1;             // 把 a 的值变为 1
    }
}
```
从上面代码中可以看出定义一个变量首先要确定变量的类型，再给变量起个好听的名字，最后给变量赋值。<br>
变量为什么叫 "变"量，是因为他是可以改变的，也就是可以重新赋值。看例子。
```java
public class Variable {

    public static void main(String[] args) {
        
        int i = 10;               // 定义一个 int 类型变量并赋值为 10
        System.out.println(i);    // 打印 这个变量  输出 10
        i = 11;                   // 重新赋值 11
        System.out.println(i);    // 打印 这个变量  输出 11 i 是可以重新赋值的
    }
}
```
## 变量的作用范围
成员变量和局部变量最大的区别是作用范围不一样
```java
public class Variable {
    
    public static void main(String[] args) {
        
        int i = 10;            
        if (true) {
            int b = 20;
            System.out.println(num);
            System.out.println(i);
            System.out.println(b);
        }
        // System.out.println(b); 编译报错 b 不在这个括号的作用范围内
    }
}
```
上述代码中 ``i`` 的作用范围在他所在大括号内<br>
![i的作用范围](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/0d9a8c7c0e67463b98adf5bd28198d2a.png?x-oss-process=style/codedm)
<br>
``b`` 的作用范围在他所在大括号内<br>
![b的作用范围](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/17b89f73462c492c9e608750f31a6119.png?x-oss-process=style/codedm)

<br> ``i`` 的作用范围要比 ``b`` 的作用范围更广。
# Java中基本数据类型
> 对应代码 -> com.codedm.java.basic.datatype.PrimitiveDataType

# Java中有8种基本数据类型：
## byte
存储从 -128 到 127 的整数 <br>
占用字节数： 1 byte
```
byte b = 100;
```
## short
存储从 -32,768 到 32,767 的整数 <br>
占用字节数： 2 byte
```
short s = 100;
```
## int
存储从 -2,147,483,648 到 2,147,483,647 的整数 <br>
占用字节数： 4 byte
```
int t = 100;
```
## long
存储从 -9,223,372,036,854,775,808 到 9,223,372,036,854,775,807 的整数 <br>
占用字节数： 8 byte
```
long l = 100;
```
## float
存储一个分数，能够储存6至7位的小数 <br>
占用字节数： 4 byte
```
float f = 100.1f;
```
## double
存储一个分数，能够储存15位的小数 <br>
占用字节数： 8 byte
```
double d = 100.1;
```
## boolean
存储一个 ``true`` 和 ``false`` <br>
占用字节数： 1 bit
```
boolean bl = true;
```
## char
存储单个字符  <br>
占用字节数： 2 byte
```
char c = 'C';
```
<br>
从byte -> short -> int -> long 虽然存储的都是整数但是能够存储的位数是不一样的，位数越小占用的空间也就越小，选择合适的变量能够优化程序运行时占用的内存空间。
我们假设 ``1 byte`` 代表一个空格。
![占用存储空间](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/646efe8dcaa546f9a339774f1035ab66.png?x-oss-process=style/codedm)
<br>
虽然 ``int``也是存储 ``1`` 这个值，但是他占用了4个格子，即使格子是空的也会占用内存空间。

# Java 中的引用数据类型
除了8大基本数据类型其他的全部是引用数据类型<br>
## String
``String`` 类型用来存储一段文本。(该类后面会详细讲解)
```
    String name = "Codedm";
```
## 数组
多个相同的数据类型组成的集合。<br>
> 数学中集合的概念是：集合是指具有某种特定性质的具体的或抽象的对象汇总而成的集体。其中，构成集合的这些对象则称为该集合的元素。

<br>
数组和集合的概念类似：多个元素的汇总而形成的集体。<br>
举个例子：给老王家送苹果，我想送10个苹果，一个一个的送会很麻烦。

![送苹果给老王家](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/017245ad8e0d43519b1f5d4be3bf034c.png?x-oss-process=style/codedm)

怎么办呢，把10个苹果放在一个篮子中，拿着篮子取老王家。<br>
![篮子苹果](https://codedm.oss-cn-hangzhou.aliyuncs.com/images/20220730/0f374dd263304aa489fd5e81522a42a3.png?x-oss-process=style/codedm)

例子中的篮子就是数组，苹果是数组中的一个一个元素。<br>
Java 中的数组是不可变的，像这个篮子一样制作完成之后篮子的大小就不能改变了。<br>
### 初始化数组
```
int[] ints = {1, 2, 3, 4};        // 初始化方式 1
String[] strings = new String[3]; // 初始化方式 2
strings[0] = "Hello";             // 下标从 0 开始，向第 0 个位置添加字符串 Hello
strings[1] = " ";                 // 向第 1 个位置添加字符串 空格
strings[2] = "World";             // 向第 2 个位置添加字符串 World
// strings[3] = "Hello"; // 报错
System.out.println(strings[0]);   // 获取第 0 个元素并打印到控制台  
```

## 包装类
上述的8大基本数据类型都有对应的包装类(先了解，后面讲完对象之后会详细讲解。)
- ``byte`` -> ``Byte``
- ``short`` -> ``Short``
- ``int`` -> ``Integer``
- ``long`` -> ``Long``
- ``float`` -> ``Float``
- ``double`` -> ``Double``
- ``boolean`` -> ``Boolean``
- ``char`` -> ``Character``
