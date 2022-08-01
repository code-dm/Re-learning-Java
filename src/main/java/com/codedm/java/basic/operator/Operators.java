package com.codedm.java.basic.operator;

/**
 * Java 中的运算符
 *
 * @author Codedm
 */
public class Operators {

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = a + b; // 3
        int d = b - a; // 1

        // i++ 和 ++i 的区别
        int i = 1;
        int j = ++i;
        System.out.println(j);

        int x = 1;
        int y = x++;
        System.out.println(y);

        // 比较运算符
        boolean t = 2 > 1;
        System.out.println(t); // true
        // &&
        System.out.println(2 > 1 && 3 > 1); // true
        System.out.println(2 > 1 && 0 > 1); // false

        // ||
        System.out.println(2 > 1 || 3 > 1); // true
        System.out.println(2 > 1 || 0 > 1); // true

        System.out.println("----------------------------------");
        System.out.println(10 & 12);
        System.out.println(1 & 0);
        System.out.println(0 & 1);
        System.out.println(1 & 1);
        System.out.println(0 & 0);
        System.out.println(0 & 0);

        System.out.println(true & true); // true
        System.out.println(false & true); // false
        System.out.println(false & false); // false

        // 三元运算符
        int z = 2 > 1 ? 10 : 11;
        System.out.println(z);



    }

}
