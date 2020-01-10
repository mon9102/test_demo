package com.testDemo.jdk8.enums;

import org.junit.jupiter.api.Test;

/**
 * @Auther: zouren
 * @Date: 2020/1/10 17:23
 * @Description:
 */
public class SwitchTest {
    @Test
    public void switchTest(){
        int i=1;
        switch(i){
            case 1:
                //语句
                System.out.println(1);
                break; //可选
            case 2 :
                //语句
                System.out.println(2);

                break; //可选
            //你可以有任意数量的case语句
            default : //可选
                //语句
        }
    }
}
