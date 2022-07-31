package com.codedm.java.generics.print;

/**
 * @author Dongming WU
 */
public class ObjectReturnPrint {

    private final Object thingToPrint;

    public ObjectReturnPrint(Object thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    // 需要返回打印的对象
    public Object print() {
        System.out.println(thingToPrint);
        return thingToPrint;
    }
}
