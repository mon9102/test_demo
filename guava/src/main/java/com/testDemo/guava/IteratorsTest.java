package com.testDemo.guava;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:21
 * @Description:
 * Iterators是Guava中对Iterator迭代器操作的帮助类，这个类提供了很多有用的方法来简化Iterator的操作。
 */
public class IteratorsTest {
    /**
     * all方法的第一个参数是Iterator，第二个参数是Predicate<String>的实现，
     * 这个方法的意义是不需要我们自己去写while循环了，
     * 他的内部实现中帮我们做了循环，把循环体中的条件判断抽象出来了。
     */
    @Test
    public void all(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        boolean allIsStartsWithP = Iterators.all(list.iterator(), condition);
        System.out.println("all result == " + allIsStartsWithP);
    }

    /**
     * 匹配一个
     */
    @Test
    public void any(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        boolean allIsStartsWithP = Iterators.any(list.iterator(), condition);
        System.out.println("any result == " + allIsStartsWithP);
    }
    @Test
    public void get(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        String secondElement =  Iterators.get(list.iterator(), 1);
        System.out.println("secondElement == " + secondElement);
    }

    /**
     *  filter方法过滤符合条件的项
     */
    @Test
    public void filter(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        //filter方法的第一个参数是源迭代器，第二个参数是Predicate的实现，其apply方法会返回当前元素是否符合条件
        Iterator<String> startPElements = Iterators.filter(list.iterator(), new Predicate<String>() {
            public boolean apply(String input) {
                return input.startsWith("P");
            }
        });
        while(startPElements.hasNext()){
            System.out.println(startPElements.next());
        }
    }
    /**
     *   find方法返回符合条件的第一个元素
     */
    @Test
    public void find(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        String length5Element = Iterators.find(list.iterator(), new Predicate<String>() {
            public boolean apply(String input) {
                return input.length() == 5;
            }
        });
        System.out.println(length5Element);

    }
    /**
     *   例子中我们将字符串转换成了其长度，transform方法输出的是另外一个Iterator.
     */
    @Test
    public void transform(){
        List<String> list = Lists.newArrayList("Apple","Pear","Peach","Banana");

        Predicate<String> condition = new Predicate<String>() {
            public boolean apply(String input) {
                return ((String)input).startsWith("P");
            }
        };
        Iterator<Integer> countIterator = Iterators.transform(list.iterator(), new Function<String, Integer>() {
            public Integer apply(String input) {
                return input.length();
            }
        });
        while(countIterator.hasNext()){
            System.out.println(countIterator.next());
        }

    }

}
