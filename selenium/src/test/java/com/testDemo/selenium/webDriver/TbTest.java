package com.testDemo.selenium.webDriver;

import cn.hutool.setting.dialect.Props;
import com.testDemo.selenium.Application;
import com.testDemo.selenium.reuse.ReuseWebDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;

/**
 * @Auther: zouren
 * @Date: 2019/5/9 17:06
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TbTest extends IeDriverLisTest {
    @Test
    public void login() throws MalformedURLException {
        ieDriverLis.login();
    }
    @Test
    public void NoScanContInput() throws MalformedURLException {
        Props props = new Props(testPropConfig.getProp() + testPropConfig.getFils().get("webDriver"));
        String sid = props.getStr("sessionid");
        String url = props.getStr("serverUrl");
        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
        ieDriverLis.NoScanContInput(driver);
    }

    @Test
    public void tbALL() throws MalformedURLException, InterruptedException {
        ieDriverLis.index("http://sinosoft.vicp.hk/dev/", "001", "001");
        Props props = new Props(testPropConfig.getProp() + testPropConfig.getFils().get("webDriver"));
        String sid = props.getStr("sessionid");
        String url = props.getStr("serverUrl");

        ReuseWebDriver driver = new ReuseWebDriver(url, sid);

        do {
            try {
                ieDriverLis.chageMenu(driver, "/NoScanContInput.jsp");
            } catch (Exception e) {
                ieDriverLis.chageMenu(driver, "/whatsnew.jsp");
                System.out.println("continue");
                continue;
            }
            System.out.println("break");
            break;

        } while (true);


        ieDriverLis.NoScanContInput(driver);

    }
}
