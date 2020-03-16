package com.testDemo.jdk8.function;

import jdk.nashorn.internal.objects.annotations.Function;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Auther: zouren
 * @Date: 2019/3/26 14:10
 * @Description:
 */
public interface IServer {
    Map aa(String a);
    Map cc(String c);
    default Map bb(String b){
        Map re = new HashMap(10);
        re.put("bb",b);
        return re;
    }
}
