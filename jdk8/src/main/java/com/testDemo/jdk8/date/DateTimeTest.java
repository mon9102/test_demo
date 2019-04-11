package com.testDemo.jdk8.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: zouren
 * @Date: 2019/4/10 11:07
 * @Description:
 */
public class DateTimeTest {
    public static void main(String[] args) {
        LocalDateTime Idt4= LocalDateTime.now();

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        System.out.println(  Idt4.format(formatter).replaceAll(" ", "T"));
    }
}
