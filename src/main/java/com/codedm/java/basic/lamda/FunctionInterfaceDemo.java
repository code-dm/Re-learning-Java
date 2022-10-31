package com.codedm.java.basic.lamda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dongming WU
 */
public class FunctionInterfaceDemo {

    public static void main(String[] args) {

        List<String> userList = new ArrayList<>();
        userList.add("彼得");
        userList.add("鲍勃");
        userList.add("威廉");

        userList.stream()
                .filter(user -> "彼得".equals(user))
                .forEach(System.out::println);
    }
}
