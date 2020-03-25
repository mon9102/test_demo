package com.testDemo.guava;

import com.google.common.base.Preconditions;
import org.junit.platform.commons.util.StringUtils;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 13:56
 * @Description: 他的意义在于为我们提供了同一的参数校验，并对不同的异常情况抛出合适类型的异常，并对异常信息做格式化
 */
public class PreconditionsTest {
    public static void main(String[] args) {
        PreconditionsTest demo = new PreconditionsTest();
//        demo.doSomething("Jim", 19, "hello world, hello java");

        Preconditions.checkArgument(true, "age must in range (18,99)");

    }

    public void doSomething(String name, int age, String desc) {
        Preconditions.checkNotNull(name, "name may not be null");
        Preconditions.checkArgument(age >= 18 && age < 99, "age must in range (18,99)");
        Preconditions.checkArgument(desc !=null && desc.length() < 10, "desc too long, max length is ", 10);

        //do things
    }
}
