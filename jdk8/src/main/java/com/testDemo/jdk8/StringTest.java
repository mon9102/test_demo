package com.testDemo.jdk8;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @Auther: zouren
 * @Date: 2020/1/4 12:38
 * @Description:
 * 大于：&gt;
 *
 * 小于：&lt;
 *
 * 大于等于：&gt;=
 *
 * 小于等于：&lt;=
 */
public class StringTest {
    @Test
    public void containsIgnoreCase(){

        System.out.println( StringUtils.containsIgnoreCase("asdf-aa","AS"));
        System.out.println(  StringUtils.containsIgnoreCase("asdf-aa","B"));
    }
    @Test
    public void joinWith(){

        System.out.println( StringUtils.joinWith("asdf-aa","AS","CC"));
        System.out.println( StringUtils.joinWith("asdf-aa","AS"));
        System.out.println( StringUtils.appendIfMissing("asdf-aa","B"));
    }
    @Test
    public void subStr(){
        String symbol = "abvcefghijk";
        System.out.println(StringUtils.substring(symbol, -6));
        System.out.println(StringUtils.substring(symbol, 0,6));
    }
    @Test
    public void isEmty(){

        System.out.println( StringUtils.isNotEmpty(""));
        System.out.println( StringUtils.isNotEmpty(null));
        System.out.println( StringUtils.isNotEmpty(" "));
        System.out.println( "==============");
        System.out.println( StringUtils.isBlank(""));
        System.out.println( StringUtils.isNoneBlank(null));
        System.out.println( StringUtils.isNoneBlank(" "));
    }
    @Test
    public void test(){
        String symbol = "EURUSD-STD";
        String sexCode =  StringUtils.substring(symbol, 0,6);
        System.out.println(sexCode);
        System.out.println( StringUtils.appendIfMissing(sexCode,""));
        System.out.println( StringUtils.appendIfMissing("",sexCode));
    }
    @Test
    public void tet111(){
        System.out.println(StringUtils.startsWith("a123","a"));
        System.out.println(StringUtils.startsWith("","a"));
        System.out.println(StringUtils.startsWith(null,"a"));
    }
}
