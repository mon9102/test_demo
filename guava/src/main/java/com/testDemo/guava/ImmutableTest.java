package com.testDemo.guava;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:03
 * @Description: 不可变集合,不可变集合的使用和普通集合一样，只是不能使用他们的add，remove等修改集合的方法。
 */
public class ImmutableTest {
    public void createImmutableSet(){
        //builder
        Set<String> immutableNamedColors = ImmutableSet.<String>builder()
                .add("red", "green","black","white","grey")
                .build();
        //使用of静态方法创建
//        ImmutableSet.of("red","green","black","white","grey");
        //使用copyOf静态方法创建
//        ImmutableSet.copyOf(new String[]{"red","green","black","white","grey"});
        //immutableNamedColors.add("abc");
        for (String color : immutableNamedColors) {
            System.out.println(color);
        }
    }
    public void createImmutableCollection(){
        //builder
        ImmutableCollection<String> immutableNamedColors = ImmutableSet.<String>builder()
                .add("red", "green","black","white","grey")
                .build().asList();
        for (String color : immutableNamedColors) {
            System.out.println(color);
        }
    }
}
