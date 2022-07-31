title: 第一个 Java 程序 - Hello World
date: 2022-07-26 00:00:00
toc: true
categories: 
- Java基础
---

[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/6)
前两篇文章我们了解了什么是Java以及Java在各个平台的安装过程，这篇起，我们开始快乐的写Java代码，做一个Java程序员。
<!-- more -->
![](pi/Pasted%20image%2020220727214053.png)

# 新建class文件
首先随便找一个地方新建一个HelloWorld.java文件，然后用记事本打开他，Mac环境可以使用文本编辑器。
然后一个字母一个字母打出下面的代码(不准复制。。)：
```java
public class HelloWorld {
    
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```
# 命令行式运行
在当前文件夹下打开命令行工具，Windows 打开CMD，Mac打开终端。输入命令
```shell
javac HelloWorld.java
```
![](pi/Pasted%20image%2020220727214153.png)
没有报错，没有出现异常。
![](pi/Pasted%20image%2020220727214212.png)
文件夹中出现HelloWorld.class文件，这是javac编译后的可运行文件。
执行class文件：
```shell
java HelloWorld
```
![](pi/Pasted%20image%2020220727214245.png)
控制台会打印出HelloWorld，程序运行成功。
# Idea开发工具运行
下载安装Idea（略过，自己百度去），我还是给你们留了一个教程连接：[安装教程](https://www.lyscms.info/blog/detail/1305706939601592322)
安装教程创建完一个project，在project的src/main/java下创建一个类，命名HelloWorld。
![](pi/Pasted%20image%2020220727214350.png)
输入代码
![](pi/Pasted%20image%2020220727214406.png)
点击绿色箭头运行，下方控制台会出现我们程序要求打印的Hello World
![](pi/Pasted%20image%2020220727214428.png)
![](pi/Pasted%20image%2020220727214444.png)
# 代码讲解
![](pi/Pasted%20image%2020220727214512.png)
## 注释
常用注释两种
```shell
单行注释 // 
多行注释
/**
 * 我的第一个Java程序
 *
 * @author Code-dm
 */
```
## 类（class）
类名必须和文件名一样。暂时先理解为一个代码容器，后续的面向对象会详细讲解。
## Main方法
public static void main(String[] args)<br />程序的主入口，一个程序只能有一个主入口。且每个程序都必须包含一个 `main()` 方法。
## 打印
`System.out.println()`可以将字符串打印到控制台中。

- `System`是Java核心库中的一个类
- `out`是控制输出的对象
- `println()`接收一个字符串并通过`write`输出到控制台


