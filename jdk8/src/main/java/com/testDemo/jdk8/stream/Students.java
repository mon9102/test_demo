package com.testDemo.jdk8.stream;

import lombok.Data;

import java.util.List;

/**
 * @Auther: zouren
 * @Date: 2020/5/25 17:44
 * @Description:
 */
@Data
public class Students {
    private String name;
    private List<Student> studentList;
}
