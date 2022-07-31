package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class StringPrint {

    private final String thingToPrint;

    public StringPrint(String thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public void print() {
        System.out.println(thingToPrint);
    }
}
