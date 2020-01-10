package com.testDemo.jdk8.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: zouren
 * @Date: 2019/4/10 11:07
 * @Description:
 */
public class DateTimeTest {
    public static void main(String[] args) {
        LocalDateTime Idt4 = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        System.out.println(Idt4.format(formatter).replaceAll(" ", "T"));
    }

    @Test
    public void test1() {
        LocalDate now = LocalDate.of(2019, 12, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        System.out.println(now.format(formatter));


    }

    @Test
    public void test2() {
        LocalDate now = LocalDate.of(2019, 12, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        System.out.println(now.format(formatter));
    }
}
