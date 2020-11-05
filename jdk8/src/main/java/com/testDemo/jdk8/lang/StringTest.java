package com.testDemo.jdk8.lang;

import org.junit.jupiter.api.Test;

/**
 * @Auther: zouren
 * @Date: 2019/7/24 10:13
 * @Description:
 */
public class StringTest {
    @Test
    public  void format() {
        System.out.println("B");
        System.out.println(String.format("%%%s%%","aa"));
        System.out.println("aa");
        System.out.println(String.format("%s%%", "aa"));

    }
}
