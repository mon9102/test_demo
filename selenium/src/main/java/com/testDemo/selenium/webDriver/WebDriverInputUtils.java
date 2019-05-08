package com.testDemo.selenium.webDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 15:09
 * @Description:
 */
public class WebDriverInputUtils {

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
}
