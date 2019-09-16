package com.testDemo.jdk8.stream;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: zouren
 * @Date: 2019/7/9 10:25
 * @Description:
 */
public class MapTest {
    public Map<String,String> getMap(){
        Map<String,String> re= new HashMap();
        re.put("a","1");
        re.put("b","2");
        re.put("c","3");
        re.put("d","4");
        return re ;
    }
    @Test
    public void testJoin(){
        Map<String,String> m =  getMap();
       String a = m.entrySet().stream().map(entry->String.format("%s=%s", entry.getKey(), entry.getValue()))
               .collect(Collectors.joining("&"));
//        a=1&b=2&c=3&d=4
        System.out.println(a);
    }
}
