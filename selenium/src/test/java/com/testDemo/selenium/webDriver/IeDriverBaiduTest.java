package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 13:38
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IeDriverBaiduTest {
    @Autowired
    private IeDriverBaidu ieDriverBaidu;
//    @Autowired
//    private MyIEDriver myIEDriver;
    @Test
    public void getBody(){
//        WebElement webElement = ieDriverBaidu.getBody2("https://www.baidu.com","学习");
//        System.out.println(webElement.getText());
        System.out.println("getBody");
    }
    @Test
    public void getBody2(){
        WebElement webElement = ieDriverBaidu.getBody2("https://www.baidu.com","学习");
        System.out.println(webElement.getText());
    }
    @Test
    public void getBody2By360() throws MalformedURLException {
        String sid = "d2e365aa-d384-49ca-af3a-02a67771372e";
        String url = "http://localhost:19653";


        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
//
        ieDriverBaidu.getBody2By360(driver,"https://www.so.com/","学习");
    }
}
