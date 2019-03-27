package com.testDemo.jdk8.function;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @Auther: zouren
 * @Date: 2019/3/26 13:47
 * @Description: BiFunction<T, U, R>中的T, U表示接口输入的第一、第二个参数、R是输出的数据类型
 */
public class BiFunctionTest {
    public static void main(String[] args) {
        Server s = new Server();

        BiFunction<IServer, String, Map> biFunction = (t, u) -> {

            return  Optional.ofNullable(t).map(server->server.aa(u)).orElse(null);
        };
        BiFunction<IServer, String, Map> biFunction1 = (t, u) -> {

            return Optional.ofNullable(t).map(server->server.cc(u)).orElse(null);
        };
        BiFunction<IServer, String, Map> biFunction2 = (t, u) -> {

            return Optional.ofNullable(t).map(server->server.bb(u)).orElse(null);
        };
        Map<String,String> map =  biFunction.apply(s,"a");
        System.out.println(map);
        Map<String,String> mapbb =  biFunction2.apply(s,"b");
        System.out.println(mapbb);
        s=null;
        Map<String,String> map1 = biFunction1.apply(s,"c");
        Optional<Map> m1 =  Optional.ofNullable(map1);
        if (m1.isPresent()){
            System.out.println(map1);
        }

    }
}
