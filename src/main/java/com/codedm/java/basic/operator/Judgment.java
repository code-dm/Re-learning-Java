package com.codedm.java.basic.operator;

import java.util.Scanner;

/**
 * @author Dongming WU
 */
public class Judgment {

    public static void main(String[] args) {
        // 接收控制台输入
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // equals方法可以比较两个字符串是否一样
        if ("A".equals(input)) {
            System.out.println("AA");
        } else if ("B".equals(input)) {
            System.out.println("BB");
        } else {
            System.out.println("CC");
        }
    }
}
