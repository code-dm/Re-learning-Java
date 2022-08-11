package com.codedm.java.basic.oop;

import java.util.Scanner;

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
        int weight = sc.nextInt();
        int totalPrice = Integer.parseInt(fruitData[fruitNumber][1]) * weight;
        System.out.println("总价为：" + totalPrice);

    }
}
