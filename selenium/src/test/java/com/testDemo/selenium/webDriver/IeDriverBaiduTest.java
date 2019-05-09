package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.Application;
import com.testDemo.selenium.reuse.ReuseWebDriver;
import com.testDemo.selenium.utils.TabUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;

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
    public void ReuseWebDriver() throws MalformedURLException {
        String sid = "d2e365aa-d384-49ca-af3a-02a67771372e";
        String url = "http://localhost:19653";


        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
//
        ieDriverBaidu.getBody2By360(driver,"https://www.so.com/","学习");
    }
    @Test
    public void tabTest(){
        ieDriverBaidu.getBody2("https://www.baidu.com","学习");
        TabUtils.openTab(IeDriverBaidu.browser,"https://www.so.com/");
        String handle = TabUtils.findNewTab(IeDriverBaidu.browser);

//        ieDriverBaidu.getBody2By360(IeDriverBaidu.browser,"https://www.so.com/","学习");
    }
}
