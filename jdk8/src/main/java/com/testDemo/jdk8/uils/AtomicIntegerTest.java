package com.testDemo.jdk8.uils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: zouren
 * @Date: 2019/9/16 16:33
 * @Description:
 */
public class AtomicIntegerTest {
    @Test
    public void addAndGet(){
        AtomicInteger allItem = new AtomicInteger();
        for (int i=0;i<10;i++){
            allItem.addAndGet(2);

        }
        System.out.println(allItem.get());

    }
}
