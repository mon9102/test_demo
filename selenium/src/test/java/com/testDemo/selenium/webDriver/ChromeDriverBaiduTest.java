package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 11:46
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ChromeDriverBaiduTest {
    @Autowired
    private ChromeDriverBaidu chromeDriverBaidu;
    @Test
    public void getBody(){
        WebElement webElement = chromeDriverBaidu.getBody("https://www.baidu.com","学习");
        System.out.println(webElement.getText());
    }
}
