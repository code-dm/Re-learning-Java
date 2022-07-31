package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class ObjectPrint {

    private final Object thingToPrint;

    public ObjectPrint(Object thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public void print() {
        System.out.println(thingToPrint);
    }
}
