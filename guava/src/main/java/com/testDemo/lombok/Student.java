package com.testDemo.lombok;

import lombok.*;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 16:14
 * @Description:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class Student {
    @NonNull
    private int id;
    private String name;
    private int age;
    public static void main(String[] args) {
        new Student(1, "circle",20);
        //使用静态工厂方法
        Student a =  Student.of(1);
        //无参构造
        new Student();
        //包含所有参数
        new Student(1, "circle", 2);
        System.out.println(a);
    }
}
