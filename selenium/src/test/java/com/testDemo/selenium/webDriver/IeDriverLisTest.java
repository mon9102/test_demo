package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.Application;
import com.testDemo.selenium.reuse.ReuseWebDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 15:26
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IeDriverLisTest {
    @Autowired
    private IeDriverLis ieDriverLis;


    @Test
    public void index(){
        ieDriverLis.index("http://sinosoft.vicp.hk/dev/","001","001");
    }
    @Test
    public void chageMenu() throws MalformedURLException {
        String sid = "9f6bfe6d-b54a-42da-a5c7-d697c7444f2a";
        String url = "http://localhost:16368";
        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
        ieDriverLis.chageMenu(driver,"../app/NoScanContInput.jsp?type=1");
    }
}
