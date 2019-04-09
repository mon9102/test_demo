package com.testDemo.guava;

import com.google.common.base.Throwables;

import java.sql.SQLException;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 13:58
 * @Description:
 */
public class ThrowablesTest {
    public void doSomething() throws Throwable {
        //ignore method body
    }

    /**
     * doSomething方法，而doIt的定义中只允许抛出SQLException，否则将抛出RuntimeException，我们可以这样做
     *
     * @throws SQLException
     */
    public void doIt() throws SQLException {
        try {
            doSomething();
        } catch (Throwable throwable) {
            Throwables.propagateIfPossible(throwable, SQLException.class);
        }
    }
}
