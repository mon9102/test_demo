package com.testDemo.selenium.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @Auther: zouren
 * @Date: 2019/5/9 14:33
 * @Description:
 */
public class JavascriptUtil {
    public static Object apply(WebDriver browser, String script){
        JavascriptExecutor executor = (JavascriptExecutor) browser;
        return executor.executeScript(script);
    }
}
