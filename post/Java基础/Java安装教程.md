title: Java 安装教程
date: 2022-07-25 00:00:00
toc: true
excerpt: 这是一篇关于如何安装Java的教程
categories: 
- Java基础
cover: pi/Pasted%20image%2020220727213852.png
---

[本篇文章对应的Issues](https://github.com/Code-dm/Re-learning-Java/issues/1)

<!-- more -->

## 下载JDK

[下载地址](https://www.oracle.com/java/technologies/downloads/)

下载最新版本JDK不用登录账号，但是下载JDK1.8需要登录账号，如果不想注册，可以使用我提供的账号（贴心不 :smirk: ）。

```
1414807686@qq.com
Codedm96@codedm.com
```

下载页面往下拉可以看见Java8，根据您不同系统下载不同版本的JDK。

## Windows安装JDK

- 双击打开安装包
  
  ![点击安装](https://s2.loli.net/2022/07/24/cz9nY54x1A3Lk8S.jpg)  

- 点击下一步
  
  ![安装指导界面](https://s2.loli.net/2022/07/24/z5PowLs3Of1WiDq.jpg)

- 点击下一步
  
  ![正在安装](https://s2.loli.net/2022/07/24/cdmSZO6YPKyNWBU.jpg)

  ![继续安装](https://s2.loli.net/2022/07/24/c3UhVdIbzWtKyQv.jpg)

- 点击下一步

  ![401658675238_.pic.jpg](https://s2.loli.net/2022/07/24/PEHrJkqytmcbz6i.jpg)

  ![安装成功](https://s2.loli.net/2022/07/24/u8KGQMU4NzV1fSY.jpg)

- 设置中搜索 环境变量 -> 点击编辑系统环境变量 -> 点击环境变量
  
  ![451658675239_.pic.jpg](https://s2.loli.net/2022/07/24/HYv6EOLMbDil21A.jpg)

  ![461658675239_.pic.jpg](https://s2.loli.net/2022/07/24/K3fa4H7Azp2lYQy.jpg)

- 点击新建
  
  ![471658675239_.pic.jpg](https://s2.loli.net/2022/07/24/4THEioCaGLRz3xK.jpg)
  
- 新建Java Home
  
  > 默认安装路径：C:\Program Files\Java\ 下面的变量值建议到这个文件夹中复制，小版本号可能会存在差异。

  变量名：JAVA_HOME
  变量值：C:\Program Files\Java\jdk1.8.0_341

  ![481658675239_.pic.jpg](https://s2.loli.net/2022/07/24/DQeFp2GbfL6arCu.jpg)

- 新建CLASSPATH
  
  变量名：CLASSPATH
  变量值：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;

  ![491658675240_.pic.jpg](https://s2.loli.net/2022/07/24/1wdfK8GS9iC62Bm.jpg)

- 修改Path(注意是修改)
  
  修改界面点击新建

  ![501658675240_.pic.jpg](https://s2.loli.net/2022/07/24/IAa4ijNt6nheHBx.jpg)
  
  新建两个：
  ```
  %JAVA_HOME%\bin
  %JAVA_HOME%\jre\bin
  ```
  ![511658675240_.pic.jpg](https://s2.loli.net/2022/07/24/DPcrl5REb1673S4.jpg)

- 所有窗口点击确定

## MacOS安装JDK

- 下载完成之后双击打开，再双击黄色盒子
  
  ![JDK安装](https://s2.loli.net/2022/07/24/lxOkVPhb4oG8esF.png)

  ![JDK安装](https://s2.loli.net/2022/07/24/Jzh8xormR67i9Gp.png)

- 点击继续 -> 点击安装 -> 点击关闭
  
  ![JDK正在安装](https://s2.loli.net/2022/07/24/lzysqbh8pMRBPai.png)

  ![JDK安装成功](https://s2.loli.net/2022/07/24/TWa2pRcPey1LKMn.png)

- 配置环境变量
  
  打开终端输入下面命令

  ``` vim .zshrc ```

  按下 i 键，再粘贴输入(注意修改成你自己的路径)：

  ```
  JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_341.jdk/Contents/Home
  PATH=$JAVA_HOME/bin:$PATH:.
  CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:.
  export JAVA_HOME
  export PATH
  export CLASSPATH
  ````

  > 注意修改成你自己的版本号jdk1.8.0_341.jdk

  按键盘 esc -> 输入 :wq -> 回车

  再输入命令

  ``` 
  source .zshrc
  ```
  
  > 默认安装路径为：
  /Library/Java/JavaVirtualMachines/jdk1.8.0_341.jdk/Contents/Home


## Linux安装JDK

## 测试安装

命令行输入：
```
java -version
```

显示出版本号则证明安装成功。

```
java version "1.8.0_341"
Java(TM) SE Runtime Environment (build 1.8.0_341-b10)
Java HotSpot(TM) 64-Bit Server VM (build 25.341-b10, mixed mode)
```

命令行输入java以及javac均不报错：

``` 
java
```

以及

```
javac
```

![531658675240_.pic.jpg](https://s2.loli.net/2022/07/24/9zBxkfyrZdDCN1s.jpg)