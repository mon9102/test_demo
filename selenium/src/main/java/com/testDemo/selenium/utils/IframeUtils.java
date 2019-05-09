package com.testDemo.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @Auther: zouren
 * @Date: 2019/5/9 09:34
 * @Description:
 */
public class IframeUtils {
    /**
     * 切换到某个iframe里
     * @param browser
     * @param iframeCssSelector
     * @return
     */
    public static WebDriver chageIframe(WebDriver browser, String iframeCssSelector){
        return browser.switchTo().frame(browser.findElement(By.cssSelector(iframeCssSelector)));
    }

    /**加入有多层iframe的回到上一层
     *
     * @param browser
     * @return
     */
    public static WebDriver parenIframe(WebDriver browser ){
        return browser.switchTo().parentFrame();
    }
    /**
     *  回到主文档
     * @param browser
     * @return
     */
    public static WebDriver topPage(WebDriver browser ){
        return browser.switchTo().defaultContent();
    }

}
