package com.testDemo.guava;

import com.google.common.collect.*;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 15:22
 * @Description:
 */
public class CollectionInitTest {
    public static void main(String[] args) {

    }

    /**
     * 集合创建
     */
    public void creat() {
        Map<String, Map<String, String>> map = Maps.newHashMap();

        List<List<Map<String, String>>> list = Lists.newArrayList();

//1,简化集合的创建
        List<Person> personList = Lists.newLinkedList();
        Set<Person> personSet = Sets.newHashSet();
        Map<String, Person> personMap = Maps.newHashMap();
        Integer[] intArrays = ObjectArrays.newArray(Integer.class, 10);
    }

    /**
     * 集合初始化
     */
    public void init() {
        Set<String> set = Sets.newHashSet("one", "two", "three");

        List<String> list = Lists.newArrayList("one", "two", "three");

        Map<String, String> map = ImmutableMap.of("ON", "TRUE", "OFF", "FALSE");

        //2,简化集合的初始化
        List<Person> personList2 = Lists.newArrayList(new Person(1, 1, "a", "46546", 1, 20),

                new Person(2, 1, "a", "46546", 1, 20));
        Set<Person> personSet2 = Sets.newHashSet(new Person(1, 1, "a", "46546", 1, 20),

                new Person(2, 1, "a", "46546", 1, 20));
        Map<String, Person> personMap2 = ImmutableMap.of("hello", new Person(1, 1, "a", "46546", 1, 20), "fuck", new Person(2, 1, "a", "46546", 1, 20));
    }
}

class Person {
    private int group;
    private int id;
    private String name;
    private String name1;
    private int a;
    private int b;

    public Person(int group, int id, String name, String name1, int a, int b) {
        this.group = group;
        this.id = id;
        this.name = name;
        this.name1 = name1;
        this.a = a;
        this.b = b;

    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}