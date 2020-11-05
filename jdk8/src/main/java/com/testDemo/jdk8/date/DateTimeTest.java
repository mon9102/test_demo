package com.testDemo.jdk8.date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;

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
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        //将localDateTime转换成时间戳

        System.out.println("localDateTime:"+java.sql.Timestamp.valueOf(localDateTime).getTime());

    }
    @Test
    public void test3() {
        LocalDateTime day = LocalDateTime.of(2019, 1, 1,0,0,0);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.getYear()+" "+now.getMonthValue()+"   "+day.getYear()+" "+day.getMonthValue());
        System.out.println(now.format(formatter));
        LocalDateTime firstday = now.with(TemporalAdjusters.firstDayOfMonth());
        firstday = LocalDateTime.of(now.getYear(), now.getMonthValue(), 1, 00, 00, 00);
        LocalDateTime lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        lastDay = LocalDateTime.of(now.getYear(), now.getMonthValue(), lastDay.getDayOfMonth(), 23, 59, 59);
        LocalDateTime last  = day.plusMonths(-1);
        System.out.println("firstday:" + firstday.format(formatter));
        System.out.println("firstday:" + firstday.format(formatter));
        System.out.println("last:" + last.format(formatter));

    }
    @Test
    public void getSatrtAndEndMonthDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime time = LocalDateTime.now();
        for (int i = 0; i < 12; i++) {
            LocalDateTime[] localDateTimes = DateTimeUtils.getSatrtAndEndMonthDay(time);
            System.out.println("time:" + time.format(formatter)+"  start:"+localDateTimes[0].format(formatter)+"  end:"+localDateTimes[1].format(formatter));
            time = time.plusMonths(-1);

        }
    }

}
