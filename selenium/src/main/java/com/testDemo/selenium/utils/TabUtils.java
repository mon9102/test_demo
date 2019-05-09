package com.testDemo.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Auther: zouren
 * @Date: 2019/5/9 09:36
 * @Description:
 */
public class TabUtils {
    /**
     * 打开新的页面并返回窗口句柄，可以通句柄来切换之后操作窗口
     * @param browser
     * @param url
     * @return 窗口句柄
     */
    public static String openTab(WebDriver browser, String url){
        JavascriptExecutor executor = (JavascriptExecutor) browser;
        executor.executeScript("window.open('" + url + "')");
        return findNewTab(browser);
    }

    public static String  findNewTab(WebDriver browser){
        return findNewTab(browser,new ArrayList<String>());
    }

    /**
     *
     * @param browser
     * @param handleList  已有的句柄集合
     * @return 新的句柄
     */
    public static String  findNewTab(WebDriver browser, List<String> handleList){
        {
            //获取当前窗口句柄
            String currentWindow = browser.getWindowHandle();
            boolean flag = handleList.stream().anyMatch(handle->handle.equals(currentWindow));
            if (!flag){
                handleList.add(currentWindow);
            }
            //获取所有窗口句柄
            Set<String> handles = browser.getWindowHandles();
            String windowHandle = null;
            Iterator<String> it = handles.iterator();
            while (it.hasNext()) {
                windowHandle = it.next();
                 flag = handleList.stream().anyMatch(handle->handle.equals(currentWindow));
                if (flag) {
                    continue;
                }
                //切换到新窗口
                WebDriver window = browser.switchTo().window(windowHandle);
                System.out.println("New page title is:" + window.getTitle()+" windowHandle:"+windowHandle);
//            window.close();
            }
            return windowHandle;
            //回到原来页面
//        browser.switchTo().window(currentWindow);
//        browser.close();
        }
    }
    public static void toTab(WebDriver browser,String handle){
        browser.switchTo().window(handle);
    }
}
