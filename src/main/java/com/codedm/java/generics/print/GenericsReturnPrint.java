package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class GenericsReturnPrint<T> {

    private final T thingToPrint;

    public GenericsReturnPrint(T thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public T print() {
        System.out.println(thingToPrint);
        return thingToPrint;
    }
}
