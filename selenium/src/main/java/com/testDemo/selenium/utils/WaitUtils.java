package com.testDemo.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 15:04
 * @Description:
 */
public class WaitUtils {

    /**
     *
     * @param browser
     * @param cssSelector  与jquery选取相同
     * @return
     */
    public static boolean wiatByCssSelector(WebDriver browser,String cssSelector){
        try {
            //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
            WebDriverWait wait = new WebDriverWait(browser, 60);
            //页面元素在页面中存在
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean wiatByLinkText(WebDriver browser,String linkText){
        try {
            //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
            WebDriverWait wait = new WebDriverWait(browser, 60);
            //页面元素在页面中存在
            wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(linkText)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
