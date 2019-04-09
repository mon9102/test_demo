package com.testDemo.lombok;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 16:18
 * @Description: ctrl+F12查看实现的方法
 */
@Slf4j
public class MainTest {

    @Test
    public  void testObject(){
        Student s = new Student(1,"a",20);
        System.out.println(s);
    }
    @Test
    public  void Slf4j(){
        Student s = new Student(1,"a",20);
        log.info(s.toString());
    }

    /**
     *  @Cleanup 自动化关闭流，相当于 jdk中的 try catch  finally中close对象
     *  @SneakyThrows 当我们需要抛出异常，在当前方法上调用，不用显示的在方法名后面写 throw
     */
    @Test
    @SneakyThrows
    public  void Cleanup(){
        String path = null;
        @Cleanup
        InputStream in = new FileInputStream(path);

    }

    /**
     * 类型推导
     * ArrayList example = new ArrayList<String>();
     *  example.add("Hello, World!");
     */
    @Test
    public  void val (){
        val example = new ArrayList<String>();
        example.add("Hello, World!");
        System.out.println(example);
    }
    @Test
    public  void NonNull(){
        Student s = new Student(1,"a",20);
        System.out.println(NonNullExample(s));
        Student s1 = null;
        System.out.println(NonNullExample(s1));

    }

    /**转换后就是
     * public NonNullExample(@NonNull Student student) {
     *     if (student == null) {
     *       throw new NullPointerException("student");
     *     }
     *     this.name = student.getName();
     *  }
     *
     *
     */
    public String  NonNullExample(@NonNull Student student) {
        return student.getName();
    }
    private final Object lock = new Object();

    /**
     * 方法中所有的代码都加入到一个代码块中，默认静态方法使用的是全局锁
     * ，普通方法使用的是对象锁，当然也可以指定锁的对象
     */
    @Synchronized("lock")
    public void foo() {
        // Do something
    }
}
