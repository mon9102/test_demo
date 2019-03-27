package com.testDemo.jdk8.stream;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Auther: zouren
 * @Date: 2019/3/27 09:23
 * @Description:
 */
public class SortedTest {
    String[] arr1 = {"abc","a","bc","abcd"};
    /**
     * Comparator.comparing是一个键提取的功能
     * 以下两个语句表示相同意义
     */
    @Test
    public void testSorted1_(){
        /**
         * 按照字符长度排序
         */
        Arrays.stream(arr1).sorted((x, y)->{
            if (x.length()>y.length())
                return 1;
            else if (x.length()<y.length())
                return -1;
            else
                return 0;
        }).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.comparing(String::length)).forEach(System.out::println);
    }

    /**
     * 倒序
     * reversed(),java8泛型推导的问题，所以如果comparing里面是非方法引用的lambda表达式就没办法直接使用reversed()
     * Comparator.reverseOrder():也是用于翻转顺序，用于比较对象（Stream里面的类型必须是可比较的）
     * Comparator. naturalOrder()：返回一个自然排序比较器，用于比较对象（Stream里面的类型必须是可比较的）
     */
    @Test
    public void testSorted2_(){
        Arrays.stream(arr1).sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.reverseOrder()).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.naturalOrder()).forEach(System.out::println);
    }

    /**
     * thenComparing
     * 先按照首字母排序
     * 之后按照String的长度排序
     */
    @Test
    public void testSorted3_(){
        Arrays.stream(arr1).sorted(Comparator.comparing(this::com1).thenComparing(String::length)).forEach(System.out::println);
    }
    public char com1(String x){
        return x.charAt(0);
    }
}
