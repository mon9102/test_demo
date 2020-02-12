package com.testDemo.jdk8.stream;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @Auther: zouren
 * @Date: 2019/3/27 09:08
 * @Description:
 */
public class ListTest {
    Random random;
    List<Student> stuList;

    List<String> wordList;

    Student[] students;
    List<Map<String,BigDecimal>> mapList;
    @BeforeEach
    public void init() {
        random = new Random();
        stuList = new ArrayList<Student>() {
            {
//                for (int i = 0; i < 3; i++) {
//                    add(new Student("student" + i, random.nextInt(50) + 50));
//                    add(new Student("aa", random.nextInt(50) + 50));
//                }
            add(new Student("student" + 1, random.nextInt(50) + 50,new BigDecimal(1+"")));
            add(new Student("student" + 2, random.nextInt(50) + 50,new BigDecimal(20.2+"")));
            add(new Student("student" + 3, random.nextInt(50) + 50,new BigDecimal(300.03+"")));

            }
        };
        wordList = new ArrayList<String>() {
            {
                add("a");
                add("b");
                add("c");
                add("d");
                add("e");
                add("f");
                add("g");
            }
        };
        Map<String, BigDecimal> row = new HashMap<>();
        row.put("a",new BigDecimal("1"));
        Map<String, BigDecimal> row2 = new HashMap<>();
        row2.put("a",new BigDecimal("2"));
        mapList = new ArrayList<Map<String, BigDecimal>>() {
            {

                add(row);
                add(row2);

            }
        };
    }


    //1列出班上超过85分的学生姓名，并按照分数降序输出用户名字
    @Test
    public void test1() {
        System.out.println(stuList);
        List<String> studentList = stuList.stream()
                .filter(x -> x.getScore() > 85)
                .sorted(Comparator.comparing(Student::getScore).reversed())
                .map(Student::getName)
                .collect(toList());
        System.out.println(studentList);
    }

    @Test
    public void testCollect1() {
        /**
         * 生成List
         */
        List<Student> list = Arrays.stream(students).collect(toList());
        list.forEach((x) -> System.out.println(x));
        /**
         * 生成Set
         */
        Set<Student> set = Arrays.stream(students).collect(toSet());
        set.forEach((x) -> System.out.println(x));
        /**
         * 如果包含相同的key，则需要提供第三个参数，否则报错
         */
        Map<String, Integer> map = Arrays.stream(students).collect(toMap(Student::getName, Student::getScore, (s, a) -> s + a));
        map.forEach((x, y) -> System.out.println(x + "->" + y));

    }

    /**
     * 统计
     */
    @Test
    public void testCollect4() {
        IntSummaryStatistics summaryStatistics = Arrays.stream(students).collect(Collectors.summarizingInt(Student::getScore));
        System.out.println("getAverage->" + summaryStatistics.getAverage());
        System.out.println("getMax->" + summaryStatistics.getMax());
        System.out.println("getMin->" + summaryStatistics.getMin());
        System.out.println("getCount->" + summaryStatistics.getCount());
        System.out.println("getSum->" + summaryStatistics.getSum());
    }

    /**
     * map把一种类型的流转换为另外一种类型的流
     * 将String数组中字母转换为大写
     */
    @Test
    public void testMap() {
        String[] arr = new String[]{"yes", "YES", "no", "NO"};
        Arrays.stream(arr).map(x -> x.toLowerCase()).forEach(System.out::println);
    }

    /**
     * @Description: 过滤流，过滤流中的元素
     * @Author: zouren
     * @CreateDate: 2019/3/27 9:22
     */
    @Test
    public void testFilter() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Arrays.stream(arr).filter(x -> x > 3 && x < 8).forEach(System.out::println);
    }

    /**
     * flapMap：拆解流
     */
    @Test
    public void testFlapMap1() {
        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"e", "f", "c", "d"};
        String[] arr3 = {"h", "j", "c", "d"};
        // Stream.of(arr1, arr2, arr3).flatMap(x -> Arrays.stream(x)).forEach(System.out::println);
        Stream.of(arr1, arr2, arr3).flatMap(Arrays::stream).forEach(System.out::println);
    }

    /**
     * 延迟执行特性，在聚合操作之前都可以添加相应元素
     */
    @Test
    public void test3_1() {
        Stream<String> words = wordList.stream();
        wordList.add("END");
        long n = words.distinct().count();
        System.out.println(n);
    }

    /**
     * 延迟执行特性，会产生干扰
     * nullPointException
     */
    @Test
    public void test3_2() {
        Stream<String> words1 = wordList.stream();
        words1.forEach(s -> {
            System.out.println("s->" + s);
            if (s.length() < 4) {
                System.out.println("select->" + s);
                wordList.remove(s);
                System.out.println(wordList);
            }
        });
    }

    /**
     * 通过数组创建流
     */
    @Test
    public void testArrayStream() {
        //1.通过Arrays.stream
        //1.1基本类型
        int[] arr = new int[]{1, 2, 34, 5};
        IntStream intStream = Arrays.stream(arr);
        //1.2引用类型
        Student[] studentArr = new Student[]{new Student("s1", 29), new Student("s2", 27)};
        Stream<Student> studentStream = Arrays.stream(studentArr);
        //2.通过Stream.of
        Stream<Integer> stream1 = Stream.of(1, 2, 34, 5, 65);
        //注意生成的是int[]的流
        Stream<int[]> stream2 = Stream.of(arr, arr);
        stream2.forEach(System.out::println);
    }

    /**
     * 通过集合创建流
     */
    @Test
    public void testCollectionStream() {
        List<String> strs = Arrays.asList("11212", "dfd", "2323", "dfhgf");
        //创建普通流
        Stream<String> stream = strs.stream();
        //创建并行流
        Stream<String> stream1 = strs.parallelStream();
    }

    @Test
    public void testEmptyStream() {
        //创建一个空的stream
        Stream<Integer> stream = Stream.empty();
    }

    @Test
    public void testUnlimitStream() {
        //创建无限流，通过limit提取指定大小
        Stream.generate(() -> "number" + new Random().nextInt()).limit(100).forEach(System.out::println);
        Stream.generate(() -> new Student("name", 10)).limit(20).forEach(System.out::println);
    }

    /**
     * 产生规律的数据
     */
    @Test
    public void testUnlimitStream1() {
        Stream.iterate(0, x -> x + 1).limit(10).forEach(System.out::println);
        System.out.println("============================================");
        Stream.iterate(0, x -> x).limit(10).forEach(System.out::println);
        System.out.println("============================================");
        //Stream.iterate(0,x->x).limit(10).forEach(System.out::println);与如下代码意思是一样的
        Stream.iterate(0, UnaryOperator.identity()).limit(10).forEach(System.out::println);
        System.out.println("============================================");
    }
    @Test
    public void testTOMap() {
        stuList.stream().collect(Collectors.toMap(Student::getName,student->student));
    }
    @Test
    public void testTOMap2() {
        stuList.stream().collect(Collectors.toMap(Student::getName,student->student));

        System.out.println( stuList.stream().map(st->st.getAvgScore()).reduce((a,b)->a.add(b)).orElse(new BigDecimal(0)));
        System.out.println( stuList.stream().mapToDouble(st->st.getAvgScore().doubleValue()).sum());

    }
    @Test
    public void testFilter1(){
        List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
        System.out.println("sum is:"+nums.stream().filter(num -> num != null)
                //1,1,2,3,4,5,6,7,8,9,10
                //.peek(x -> System.out.println("peek0: "+x))
                .distinct()
                //1,2,3,4,5,6,7,8,9,10
                .mapToInt(num -> num * 2)
                //2,4,6,8,10,12
                .skip(2)
                //6,8,10,12,14,16,18,20
                .limit(4)
                .peek(System.out::println)
                //6,8,10,12
                .sum());
        //36

    }
    @Test
    public  void testRemoveIf(){
        Predicate<Student> predicate = (student) -> {
            if ("student1".equals(student.getName())){
                return true;
            }
            return false;
        };
        stuList.removeIf(predicate);
        System.out.println(stuList);
    }
    @Test
    public  void testRemoveIf2(){
        Predicate<Student> predicate = (student) -> {
            if ("student1".equals(student.getName())){
                return true;
            }
            return false;
        };
        Map<String, List<Student>> studentList = stuList.stream().filter(student ->  student.getName().startsWith("aa")).collect(Collectors.groupingBy(Student::getName));
        System.out.println(studentList);
    }
    @Test
    public void  testmapToInt(){
        DoubleSummaryStatistics qtySummary = mapList.stream().collect(Collectors.summarizingDouble(e -> Double.valueOf(e.get("a").doubleValue())));
        System.out.println(qtySummary.getSum());
    }
}
