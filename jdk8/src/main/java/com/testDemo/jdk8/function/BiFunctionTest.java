package com.testDemo.jdk8.function;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @Auther: zouren
 * @Date: 2019/3/26 13:47
 * @Description: BiFunction<T, U, R>中的T, U表示接口输入的第一、第二个参数、R是输出的数据类型
 */
public class BiFunctionTest {
    public static void main(String[] args) {
        Server s = new Server();

        BiFunction<Server, String, Map> biFunction = (t, u) -> {
            return s.aa(u);
        };
        BiFunction<Server, String, Map> biFunction1 = (t, u) -> {
            return s.cc(u);
        };
        Map<String,String> map =  biFunction.apply(s,"a");
        System.out.println(map);
        Map<String,String> map1 = biFunction1.apply(s,"c");
        System.out.println(map1);
    }
}
