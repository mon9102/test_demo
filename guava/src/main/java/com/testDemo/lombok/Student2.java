package com.testDemo.lombok;

import lombok.Builder;
import lombok.Data;

/**
 * @Auther: zouren
 * @Date: 2020/1/20 16:01
 * @Description:
 */
@Data
@Builder
public class Student2 {
    private String id;
    private String name;

    public static void main(String[] args) {
        Student2 student2 = Student2.builder().id("1").name("n1").build();
        System.out.println(student2);
    }
}
