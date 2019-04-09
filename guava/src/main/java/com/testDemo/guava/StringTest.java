package com.testDemo.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 13:40
 * @Description:
 */
public class StringTest {
    /**
     * 判断字符串是否为空
     */
    @Test
    public void isNullOrEmpty() {
        String input = "";
        boolean isNullOrEmpty = Strings.isNullOrEmpty(input);
        System.out.println("input " + (isNullOrEmpty ? "is" : "is not") + " null or empty.");
    }

    /**
     * 获得两个字符串相同的前缀或者后缀
     */
    @Test
    public void commonPrefix() {
        //前缀
        String a = "com.jd.coo.Hello";
        String b = "com.jd.coo.Hi";
        String ourCommonPrefix = Strings.commonPrefix(a, b);
        System.out.println("a,b common prefix is " + ourCommonPrefix);

        //后缀
        String c = "com.google.Hello";
        String d = "com.jd.Hello";
        String ourSuffix = Strings.commonSuffix(c, d);
        System.out.println("c,d common suffix is " + ourSuffix);
    }

    /**
     * 补全字符串
     */
    @Test
    public void pad() {
        int minLength = 4;
        String padEndResult = Strings.padEnd("123", minLength, '0');
        System.out.println("padEndResult is " + padEndResult);

        String padStartResult = Strings.padStart("1", 2, '0');
        System.out.println("padStartResult is " + padStartResult);
    }

    /**
     * Splitter类来拆分字符串
     * Splitter类可以方便的根据正则表达式来拆分字符串，可以去掉拆分结果中的空串，可以对拆分后的字串做trim操作，还可以做二次拆分。
     */
    @Test
    public void Splitter1() {
        Iterable<String> splitResults = Splitter.onPattern("[,，]{1,}")
                .trimResults()
                .omitEmptyStrings()
                .split("hello,word,,世界，水平");

        for (String item : splitResults) {
            System.out.println(item);
        }
    }

    /**
     * Splitter的onPattern方法传入的是一个正则表达式，其后紧跟的trimResults()方法表示要对结果做trim，omitEmptyStrings()表示忽略空字符串，split方法会执行拆分操作。
     * <p>
     * split返回的结果为Iterable<String>，我们可以使用for循环语句来逐个打印拆分字符串的结果。
     * <p>
     * Splitter还有更强大的功能，做二次拆分，这里二次拆分的意思是拆分两次，例如我们可以将a=b;c=d这样的字符串拆分成一个Map<String,String>。
     * 二次拆分首先是使用onPattern做第一次的拆分，然后再通过withKeyValueSeperator('')方法做第二次的拆分。
     */
    @Test
    public void Splitter2() {
        String toSplitString = "a=b;c=d,e=f";
        Map<String, String> kvs = Splitter.onPattern("[,;]{1,}").withKeyValueSeparator('=').split(toSplitString);
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
    }

    /**
     * 有拆分字符串必然就有合并字符串，guava为我们提供了Joiner类来做字符串的合并
     */
    @Test
    public void Joiner1() {
        String joinResult = Joiner.on(" ").join(new String[]{"hello", "world"});
        System.out.println(joinResult);

    }
    /**
     * Splitter方法可以对字符串做二次的拆分，对应的Joiner也可以逆向操作，将Map<String,String>做合并
     */
    @Test
    public void Joiner2() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("a", "b");
        map.put("c", "d");
        String mapJoinResult = Joiner.on(",").withKeyValueSeparator("=").join(map);
        System.out.println(mapJoinResult);

    }
}
