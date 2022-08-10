package com.codedm.java.basic.iterator;

/**
 * @author Codedm
 */
public class ForDemo {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            System.out.println(i);
            System.out.println("H");
        }

        System.out.println("循环结束");

        int j = 0;
        int c = 0;
        c = j ++;
        System.out.println(j);
        System.out.println(c);
    }
}
