package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 11:25
 * @Description:
 */
@Component
public class IeDriverBaidu {
    public static InternetExplorerDriver browser=null;
    public WebElement getBody(String url,String key){
        browser = new InternetExplorerDriver();
        String sessionid = browser.getSessionId().toString();
        System.out.println("session ID --->" + sessionid);

        URL uil = ((HttpCommandExecutor)(browser.getCommandExecutor())).getAddressOfRemoteServer();
        System.out.println("uil --->" + uil.toString());


        browser.manage().timeouts()
                .implicitlyWait(20, TimeUnit.SECONDS);
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
    public WebElement getBody2(InternetExplorerDriver browser,String url,String key){

        //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
        WebDriverWait wait = new WebDriverWait(browser, 20);
        String sessionid = browser.getSessionId().toString();
        System.out.println("session ID --->" + sessionid);

        URL uil = ((HttpCommandExecutor)(browser.getCommandExecutor())).getAddressOfRemoteServer();
        System.out.println("uil --->" + uil.toString());
        //页面元素在页面中存在
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("kw")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#kw")));
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("kw").sendKeys(key);
        browser.findElementById("su").submit();

        WebElement content =  browser.findElementById("content_left");

        return content;

    }

    /**
     *  https://www.so.com/
     * @param browser
     * @param url
     * @param key
     * @return
     */
    public WebElement getBody2By360(RemoteWebDriver browser,String url,String key){

        //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
        WebDriverWait wait = new WebDriverWait(browser, 20);
        String sessionid = browser.getSessionId().toString();
        System.out.println("session ID --->" + sessionid);

        URL uil = ((HttpCommandExecutor)(browser.getCommandExecutor())).getAddressOfRemoteServer();
        System.out.println("uil --->" + uil.toString());
        browser.get(url);
        //页面元素在页面中存在
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("kw")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#input")));
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("input").sendKeys(key);
        browser.findElementById("search-button").submit();
        WaitUtils.wiatByCssSelector(browser,"#container");
        WebElement content =  browser.findElementById("container");

        return content;

    }

    public WebElement getBody2(String url,String key){
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        internetExplorerOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, url);
        browser = new InternetExplorerDriver(internetExplorerOptions);
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
//        browser.get(url);
        System.out.println("=======================");
        //如果超过设定的最大显式等待时间阈值， 这测试程序会抛出异常。
        WebDriverWait wait = new WebDriverWait(browser, 20);
        String sessionid = browser.getSessionId().toString();
        System.out.println("session ID --->" + sessionid);

        URL uil = ((HttpCommandExecutor)(browser.getCommandExecutor())).getAddressOfRemoteServer();
        System.out.println("uil --->" + uil.toString());
        //页面元素在页面中存在
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("kw")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#kw")));
        System.out.println("Title="+browser.getTitle());
        browser.findElementById("kw").sendKeys(key);
        browser.findElementById("su").submit();

        WebElement content =  browser.findElementById("content_left");

        return content;

    }

}
