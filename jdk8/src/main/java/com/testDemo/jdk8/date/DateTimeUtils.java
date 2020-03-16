package com.testDemo.jdk8.date;



import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.TemporalAdjusters.*;

/**
 * 基于 JDK 8 time包的时间工具类
 * yyyy-MM-dd HH:mm:ss.SSS
 * @Auther: zouren
 * @Date: 2019/2/25 13:11
 * @Description:
 */
public class DateTimeUtils  {
    /**
     * 格式化当前时间
     *
     * @param format
     * @return 日期字串
     */
    public static String getCurrentDateString(String format) {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * &当前日期以 'yyyy-MM-dd'
     *
     * @return
     */
    public static String getCurrenDateString() {
        return getCurrentDateString("yyyy-MM-dd");
    }

    /**
     * 当前日期 like 'yyyy-MM-dd HH:mm:ss'
     *
     * @return
     */
    public static String getCurrenDateTimeString() {
        return getCurrentDateString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @return 当前日期 like ' yyyyMMddHHmmss'
     */
    public static String getCurrentTime() {
        return getCurrentDateString("yyyyMMddHHmmss");
    }

    /**
     * 对 data 格式化 输出
     *
     * @param date
     * @param format format – 为null时 输出格式化 yyyy-MM-dd
     * @return
     */
    public static String getDateString(Date date, String format) {
        LocalDateTime localDateTime =dateToLocalDateTime(date);
        return getDateString(localDateTime,format);
    }
    /**
     * 对 data 格式化 输出
     *
     * @param localDateTime
     * @param format format – 为null时 输出格式化 yyyy-MM-dd
     * @return
     */
    public static String getDateString(LocalDateTime localDateTime, String format) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * String 转时间
     *
     * @param dateStr 日期格式
     * @param format  时间格式
     *                           
     * @return
     */
    public static LocalDateTime parseateStr(String dateStr, String format) {

        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(format));
    }

    public static Date from(Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * java.util.Date --> java.time.LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * java.util.Date --> java.time.LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.toLocalDate();
    }
    /**
     * java.util.Date --> java.time.LocalTime
     * @param date
     * @return
     */
    public LocalTime dateToLocalTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.toLocalTime();
    }

    /**
     * java.time.LocalDateTime --> java.util.Date
     * @param localDateTime
     * @return
     */
    public static Date dateTimeTdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }
    /**
     * java.time.LocalDate --> java.util.Date
     * @param localDate
     * @return
     */
    public Date localDateTodate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
    /**
     * 获取时间间隔，并格式化为XXXX年XX月XX日
     *
     * @param lt
     *            较小时间
     * @param gt
     *            较大时间
     * @return
     */
    public static String localDateDiffFormat(LocalDate lt, LocalDate gt) {
        Period p = Period.between(lt, gt);
        String str = String.format(" %d年 %d月 %d日", p.getYears(), p.getMonths(), p.getDays());
        return str;
    }
    /**
     * 获取时间间隔（毫秒）
     *
     * @param lt
     *            较小时间
     * @param gt
     *            较大时间
     * @return
     */
    public static long millisDiff(LocalTime lt, LocalTime gt) {
        Duration d = Duration.between(lt, gt);
        return d.toMillis();
    }
    /**
     * 获取时间间隔（分钟）
     *
     * @param lt
     *            较小时间
     * @param gt
     *            较大时间
     * @return
     */
    public static long minutesDiff(LocalDate lt, LocalDate gt) {
        long minutesDiff = ChronoUnit.MINUTES.between(lt, gt);
        return minutesDiff;
    }
    /**
     * 获取时间间隔（天）
     *
     * @param lt
     *            较小时间
     * @param gt
     *            较大时间
     * @return
     */
    public static long daysDiff(LocalDate lt, LocalDate gt) {
        long daysDiff = ChronoUnit.DAYS.between(lt, gt);
        return daysDiff;
    }
    /**
     * 创建一个新的日期，它的值为上月的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfLastMonth(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_MONTH, 1).plus(-1, MONTHS));
    }
    /**
     * 创建一个新的日期，它的值为上月的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfLastMonth(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_MONTH, temporal.range(DAY_OF_MONTH).getMaximum()).plus(-1, MONTHS));


    }
    /**
     * 创建一个新的日期，它的值为当月的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.with(firstDayOfMonth());


    }
    /**
     * 创建一个新的日期，它的值为当月的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(lastDayOfMonth());
    }
    /**
     * 创建一个新的日期，它的值为下月的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfNextMonth(LocalDate date) {
        return date.with(firstDayOfNextMonth());


    }
    /**
     * 创建一个新的日期，它的值为下月的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfNextMonth(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_MONTH, temporal.range(DAY_OF_MONTH).getMaximum()).plus(1, MONTHS));


    }

    /**
     * 创建一个新的日期，它的值为上年的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfLastYear(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_YEAR, 1).plus(-1, YEARS));
    }
    /**
     * 创建一个新的日期，它的值为上年的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfLastYear(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum()).plus(-1, YEARS));
    }


    /**
     * 创建一个新的日期，它的值为当年的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        return date.with(firstDayOfYear());
    }


    /**
     * 创建一个新的日期，它的值为今年的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfYear(LocalDate date) {
        return date.with(lastDayOfYear());
    }


    /**
     * 创建一个新的日期，它的值为明年的第一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstDayOfNextYear(LocalDate date) {
        return date.with(firstDayOfNextYear());
    }


    /**
     * 创建一个新的日期，它的值为明年的最后一天
     * 
     * @param date
     * @return
     */
    public static LocalDate getLastDayOfNextYear(LocalDate date) {
        return date.with((temporal) -> temporal.with(DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum()).plus(1, YEARS));
    }


    /**
     * 创建一个新的日期，它的值为同一个月中，第一个符合星期几要求的值
     * 
     * @param date
     * @return
     */
    public static LocalDate getFirstInMonth(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(firstInMonth(dayOfWeek));
    }


    /**
     * 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期
     * 
     * @param date
     * @return
     */
    public static LocalDate getNext(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(next(dayOfWeek));
    }


    /**
     * 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期
     * 
     * @param date
     * @return
     */
    public static LocalDate getPrevious(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(previous(dayOfWeek));
    }


    /**
     * 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期，如果该日期已经符合要求，直接返回该对象
     * 
     * @param date
     * @return
     */
    public static LocalDate getNextOrSame(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(nextOrSame(dayOfWeek));
    }


    /**
     * 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星 期几要求的日期，如果该日期已经符合要求，直接返回该对象
     * 
     * @param date
     * @return
     */
    public static LocalDate getPreviousOrSame(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(previousOrSame(dayOfWeek));
    }

    /**
     * 算年龄
     * @param localDate
     * @return
     */
    public static int getAge(LocalDate localDate){
        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(localDate, today);
    }

    /**
     *  昨天= getOffsetDate(LocalDate.now(),-1)
     *  明天= getOffsetDate(LocalDate.now(),1)
     * @param localDate 日期
     * @param offsetdate 偏移天数
     * @return 对应日期 偏移后的日期
     */
    public  static LocalDate getOffsetDate(LocalDate localDate,int offsetdate){

        return localDate.with(ofDateAdjuster(date -> date.plusDays(offsetdate)));
    }

    /**
     * 对时间偏移
     * @param localDateTime
     * @param offsetHour 偏移小时
     * @return
     */
    public  static LocalDateTime getOffsetHour(LocalDateTime localDateTime,int offsetHour){

        return  localDateTime.minusHours(offsetHour);
    }
    /**
     * 得当前时间 月初与月未 如是当月返回 当月1号到现在的日期
     * @param date  
     * @return
    */
    public  static LocalDateTime[] getSatrtAndEndMonthDay(LocalDateTime date) {
        LocalDateTime[] re = new LocalDateTime[2];
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstday = null;
        LocalDateTime lastDay = null;

        if (date.getYear() == now.getYear() && now.getMonthValue() == date.getMonthValue()) {
            //当月
            firstday = now.with(TemporalAdjusters.firstDayOfMonth());
            re[1] = now;
        } else {
            firstday = date.with(TemporalAdjusters.firstDayOfMonth());
            lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
            re[1] = LocalDateTime.of(lastDay.getYear(), lastDay.getMonthValue(), lastDay.getDayOfMonth(), 23, 59, 59);

        }
        re[0] = LocalDateTime.of(firstday.getYear(), firstday.getMonthValue(), 1, 00, 00, 00);
        return re;
    }

    public static void main(String[] args) {
        LocalDate a = getOffsetDate(LocalDate.now(),-2);

//        System.out.println(a.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        System.out.printf(String.valueOf(daysDiff(LocalDate.now(),getOffsetDate(LocalDate.now(),20))));
        LocalDate localDate1 =  LocalDate.parse("2004-02-29");
        LocalDate localDate2 =  LocalDate.of(2006,3,1);



        System.out.println(YEARS.between(localDate1,localDate2));
    }
}
