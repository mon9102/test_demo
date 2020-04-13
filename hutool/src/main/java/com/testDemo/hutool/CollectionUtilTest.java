package com.testDemo.hutool;

import cn.hutool.core.collection.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author : zouren
 * @date : 2020/4/13 10:04
 */
public class CollectionUtilTest {
    @Test
    public void test() {
        List list = new ArrayList();
        System.out.println(list.toString());
        System.out.println(CollectionUtil.isNotEmpty(list));
    }

    public void test1() {
        Object v = new ArrayList();
//        Object v =  "a";

    }
}
