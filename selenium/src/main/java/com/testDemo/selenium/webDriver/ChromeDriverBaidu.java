package com.testDemo.selenium.webDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 11:25
 * @Description:
 */
@Component
public class ChromeDriverBaidu {
    public WebElement getBody(String url,String key){
        ChromeDriver browser = new ChromeDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
        browser.get(url);
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
