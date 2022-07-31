package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class GenericsDemo2 {

    public static void main(String[] args) {
        ObjectReturnPrint printer = new ObjectReturnPrint("123");
        Object print = printer.print();
        String printResult = (String) print;
    }

}
