package com.testDemo.selenium.webDriver;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.setting.dialect.Props;
import com.testDemo.selenium.Application;
import com.testDemo.selenium.config.TestPropConfig;
import com.testDemo.selenium.reuse.ReuseWebDriver;
import com.testDemo.selenium.utils.IframeUtils;
import com.testDemo.selenium.utils.InputUtils;
import com.testDemo.selenium.utils.WaitUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 15:26
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class IeDriverLisTest {
    @Autowired
    protected IeDriverLis ieDriverLis;
    @Autowired
    protected TestPropConfig testPropConfig;

    @Test
    public void index() {

    }

    @Test
    public void allMenu() throws MalformedURLException {
        Props props = new Props(testPropConfig.getProp() + testPropConfig.getFils().get("webDriver"));
        String sid = props.getStr("sessionid");
        String url = props.getStr("serverUrl");
        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
        ieDriverLis.chageMenu(driver,"承保处理","个人保单","无扫描录入");
//        WebDriver fraMenu = IframeUtils.chageIframe(driver,"#fraMenu");
//        WebElement webElement = InputUtils.findByLinkText(fraMenu,"承保处理");
//        String id = webElement.getAttribute("id");
//        id=id.replaceAll("link","tree");
//        WebElement nextWebElement = InputUtils.find(fraMenu,"#"+id+">span:last-child");
//        System.out.println(nextWebElement.isDisplayed()+"   "+ nextWebElement.getText());
//        if(!nextWebElement.isDisplayed()){
//            webElement.click();
//        }
//        System.out.println( webElement.toString());
//        System.out.println(webElement.getText());
//
//        webElement = InputUtils.findByLinkText(fraMenu,"个人保单");
//        id = webElement.getAttribute("id");
//        id=id.replaceAll("link","tree");
//        nextWebElement = InputUtils.find(fraMenu,"#"+id+">span:last-child");
//        if( !nextWebElement.isDisplayed()){
//            webElement.click();
//        }
//        System.out.println(webElement.getText());
//
//        webElement = InputUtils.findByLinkText(fraMenu,"无扫描录入");
//        System.out.println(webElement.getText());
//        webElement.click();


    }

    @Test
    public void chageMenu() throws MalformedURLException {
        Props props = new Props(testPropConfig.getProp() + testPropConfig.getFils().get("webDriver"));
        String sid = props.getStr("sessionid");
        String url = props.getStr("serverUrl");
        ReuseWebDriver driver = new ReuseWebDriver(url, sid);
        ieDriverLis.chageMenu(driver, "/NoScanContInput.jsp");
    }


}
