package com.codedm.java.basic.operator;

/**
 * @author Codedm
 */
public class SwitchSeason {

    public static void main(String[] args) {
        int month = 8;
        String monthString;
        switch (month) {
            case 1:
            case 2:
            case 12:
                monthString = "冬季";
                break;
            case 3:
            case 4:
            case 5:
                monthString = "春节";
                break;
            case 7:
            case 8:
            case 6:
                monthString = "夏季";
                break;
            case 10:
            case 11:
            case 9:
                monthString = "秋季";
                break;
            default:
                monthString = "无法匹配！";
                break;
        }
        System.out.println(monthString);
    }
}
