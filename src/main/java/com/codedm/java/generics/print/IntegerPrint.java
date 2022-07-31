package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class IntegerPrint {

    private final Integer thingToPrint;

    public IntegerPrint(Integer thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public void print() {
        System.out.println(thingToPrint);
    }
}
