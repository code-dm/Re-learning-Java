package com.codedm.java.generics;

/**
 * @author Codedm
 */
public class GenericsFunction<T, R> {

    public static  <T, R>void test(T t, R r) {
        System.out.println("==================");
        System.out.println(t.getClass());
        System.out.println(r.getClass());
        return;
    }

    public static void main(String[] args) {
        GenericsFunction.test("", "");
        GenericsFunction.test("", 1);
        GenericsFunction.<String, String>test("", "1");

    }
}
