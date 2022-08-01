package com.codedm.java.basic.operator;

/**
 * @author Codedm
 */
public class SwitchDemo {

    public static void main(String[] args) {

        int month = 8;
        String monthString;
        switch (month) {
            case 1:
                monthString = "一月";
                break;
            case 2:
                monthString = "二月";
                break;
            case 3:
                monthString = "三月";
                break;
            case 4:
                monthString = "四月";
                break;
            case 5:
                monthString = "五月";
                break;
            case 6:
                monthString = "六月";
                break;
            case 7:
                monthString = "七月";
                break;
            case 8:
                monthString = "八月";
                break;
            case 9:
                monthString = "九月";
                break;
            case 10:
                monthString = "十月";
                break;
            case 11:
                monthString = "十一月";
                break;
            case 12:
                monthString = "十二月";
                break;
            default:
                monthString = "无法匹配！";
                break;
        }
        System.out.println(monthString);
    }
}
