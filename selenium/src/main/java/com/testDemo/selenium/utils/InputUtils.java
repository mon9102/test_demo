package com.testDemo.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 15:09
 * @Description:
 */
public class InputUtils {

    public static void setValue(WebDriver browser, String cssSeleror, String value){
        WebElement webElement =  browser.findElement(By.cssSelector(cssSeleror));
        webElement.clear();
        webElement.sendKeys(value);
    }
    public static String getValue(WebDriver browser,String cssSeleror){
        WebElement webElement =  browser.findElement(By.cssSelector(cssSeleror));
        System.out.println(webElement.getText());
        return webElement.getText();
    }
    public static void submit(WebDriver browser,String cssSeleror){
        WebElement webElement =  browser.findElement(By.cssSelector(cssSeleror));
        webElement.submit();
    }
    public static void click(WebDriver browser,String cssSeleror){
        WebElement webElement =  browser.findElement(By.cssSelector(cssSeleror));
        webElement.click();
    }
    public static WebElement find(WebDriver browser,String cssSeleror){
        WebElement webElement =null;
        try {
            WaitUtils.wiatByCssSelector(browser,cssSeleror);
            webElement =  browser.findElement(By.cssSelector(cssSeleror));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webElement;
    }
    public static WebElement findByLinkText(WebDriver browser,String linkText){
        WebElement webElement =null;
        try {
            WaitUtils.wiatByLinkText(browser,linkText);
            webElement =  browser.findElement(By.partialLinkText(linkText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webElement;
    }

}
