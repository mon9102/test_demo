package com.testDemo.jdk8.stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @Auther: zouren
 * @Date: 2019/3/27 09:29
 * @Description:
 */
public class GroupingTest {
    Student[] students;
    @BeforeEach
    public void init(){
        students = new Student[100];
        for (int i=0;i<30;i++){
            Student student = new Student("user1",i);
            students[i] = student;
        }
        for (int i=30;i<60;i++){
            Student student = new Student("user2",i);
            students[i] = student;
        }
        for (int i=60;i<100;i++){
            Student student = new Student("user3",i);
            students[i] = student;
        }

    }
    @Test
    public void testGroupBy1(){
        Map<String, List<Student>> map = Arrays.stream(students).collect(groupingBy(Student::getName));
        map.forEach((x,y)-> System.out.println(x+"->"+y));
        Map<String, List<Integer>> scoreMap = Arrays.stream(students).collect(groupingBy(Student::getName,Collectors.mapping(Student::getScore, Collectors.toList())));
        scoreMap.forEach((x,y)-> System.out.println(x+"->"+y));


    }

    /**
     * 如果只有两类，使用partitioningBy会比groupingBy更有效率
     */
    @Test
    public void testPartitioningBy(){
        Map<Boolean,List<Student>> map = Arrays.stream(students).collect(partitioningBy(x->x.getScore()>50));
        map.forEach((x,y)-> System.out.println(x+"->"+y));
    }

    /**
     * downstream指定类型
     */
    @Test
    public void testGroupBy2(){
        Map<String, Set<Student>> map = Arrays.stream(students).collect(groupingBy(Student::getName,toSet()));
        map.forEach((x,y)-> System.out.println(x+"->"+y));
    }

    /**
     * downstream 聚合操作
     */
    @Test
    public void testGroupBy3(){
        /**
         * counting
         */
        Map<String,Long> map1 = Arrays.stream(students).collect(groupingBy(Student::getName,counting()));
        map1.forEach((x,y)-> System.out.println(x+"->"+y));
        /**
         * summingInt
         */
        Map<String,Integer> map2 = Arrays.stream(students).collect(groupingBy(Student::getName,summingInt(Student::getScore)));
        map2.forEach((x,y)-> System.out.println(x+"->"+y));
        /**
         * maxBy
         */
        Map<String,Optional<Student>> map3 = Arrays.stream(students).collect(groupingBy(Student::getName,maxBy(Comparator.comparing(Student::getScore))));
        map3.forEach((x,y)-> System.out.println(x+"->"+y));
        /**
         * mapping
         */
        Map<String,Set<Integer>> map4 = Arrays.stream(students).collect(groupingBy(Student::getName,mapping(Student::getScore,toSet())));
        map4.forEach((x,y)-> System.out.println(x+"->"+y));
    }
}
