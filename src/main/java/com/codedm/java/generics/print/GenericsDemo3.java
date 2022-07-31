package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class GenericsDemo3 {

    public static void main(String[] args) {
        // 编译时期报错
        // GenericsReturnPrint<Integer> print = new GenericsReturnPrint<>("123");
        GenericsReturnPrint<String> stringGenericsReturnPrint = new GenericsReturnPrint<>("123");
        String stringPrintResult = stringGenericsReturnPrint.print();

        GenericsReturnPrint<Integer> integerGenericsReturnPrint = new GenericsReturnPrint<>(1);
        Integer integerPrintResult = integerGenericsReturnPrint.print();
    }

}
