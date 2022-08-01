package com.codedm.java.basic.datatype;

/**
 * Java 中基本数据类型
 *
 * @author Codedm
 */
public class PrimitiveDataType {

    public static void main(String[] args) {
        int i = 10;
        if(true){
            int b = 20;
        }
//        System.out.println(b);

        // 所有基本数据类型
        byte b = 100;
        short s = 100;
        int t = 100;
        long l = 100;
        float f = 100.1f;
        double d = 100.1;
        boolean bl = true;
        char c = 'C';

        // 引用数据类型
        String name = "Codedm";

        int[] ints = {1, 2, 3, 4};
        String[] strings = new String[3];
        strings[0] = "Hello";
        strings[1] = " ";
        strings[2] = "World";
//        strings[3] = "Hello"; // 报错
        System.out.println(strings[0]);
    }
}
