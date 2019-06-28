package com.testDemo.Instrumentation;

/**
 * @Auther: zouren
 * @Date: 2019/6/27 11:24
 * @Description:
 */
public class Business {
    public boolean aa(String aa){
        System.out.println("Business.aa==="+aa);
        return false;
    }
    public void doSomeThing2() {

        System.out.println("Business.doSomeThing2");


    }
}
