package com.testDemo.selenium.webDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 11:25
 * @Description:
 */
@Component
public class IeDriverBaidu {
    public WebElement getBody(String url,String key){
        InternetExplorerDriver browser = new InternetExplorerDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
        browser.get(url);
        System.out.println("=======================");
        //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
        WebDriverWait wait = new WebDriverWait(browser, 20);
        //页面元素在页面中存在
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("kw")));
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("kw").sendKeys(key);
        browser.findElementById("su").submit();

        WebElement content =  browser.findElementById("content_left");

        return content;

    }
    public WebElement getBody2(String url,String key){
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        internetExplorerOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, url);
        InternetExplorerDriver browser = new InternetExplorerDriver(internetExplorerOptions);
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
//        browser.get(url);
        System.out.println("=======================");
        //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
        WebDriverWait wait = new WebDriverWait(browser, 20);
        //页面元素在页面中存在
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("kw")));
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("kw").sendKeys(key);
        browser.findElementById("su").submit();

        WebElement content =  browser.findElementById("content_left");

        return content;

    }



}
