package com.testDemo.selenium.webDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("kw").sendKeys(key);
        browser.findElementById("su").submit();

        WebElement content =  browser.findElementById("content_left");

        return content;

    }




}
