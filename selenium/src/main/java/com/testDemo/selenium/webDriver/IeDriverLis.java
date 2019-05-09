package com.testDemo.selenium.webDriver;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.setting.dialect.Props;
import com.testDemo.selenium.config.TestPropConfig;
import com.testDemo.selenium.reuse.ReuseWebDriver;
import com.testDemo.selenium.utils.IframeUtils;
import com.testDemo.selenium.utils.InputUtils;
import com.testDemo.selenium.utils.JavascriptUtil;
import com.testDemo.selenium.utils.WaitUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 14:36
 * @Description:
 */
@Component
public class IeDriverLis {
    @Autowired
    private TestPropConfig testPropConfig;
    private InternetExplorerDriver internetExplorerDriver;
    private String indexUrl = null;
    public InternetExplorerDriver getInternetExplorerDriver(){
        if(internetExplorerDriver==null){
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, indexUrl);
            internetExplorerDriver =new InternetExplorerDriver(internetExplorerOptions);
            String sessionid = internetExplorerDriver.getSessionId().toString();
            System.out.println("session ID --->" + sessionid);

            URL serverUrl = ((HttpCommandExecutor)(internetExplorerDriver.getCommandExecutor())).getAddressOfRemoteServer();
            System.out.println("serverUrl --->" + serverUrl.toString());

        }
        return internetExplorerDriver;
    }


    public void index(String url,String username,String password){
        indexUrl = url;
        InternetExplorerDriver webDriver=getInternetExplorerDriver();
        WebDriver fraInterface = IframeUtils.chageIframe(webDriver,"#fraInterface");
        WaitUtils.wiatByCssSelector(fraInterface,"#UserCode2");
        InputUtils.setValue(fraInterface,"#UserCode2",username);
        InputUtils.setValue(fraInterface,"#PWD2",password);
        InputUtils.setValue(fraInterface,"#PWD2",password);
        Props props = new Props(testPropConfig.getProp()+testPropConfig.getFils().get("webDriver"));
        String sessionid = internetExplorerDriver.getSessionId().toString();
        URL serverUrl = ((HttpCommandExecutor)(internetExplorerDriver.getCommandExecutor())).getAddressOfRemoteServer();
        props.setProperty("username",username);
        props.setProperty("password",password);
        props.setProperty("sessionid",sessionid);
        props.setProperty("serverUrl",serverUrl);
        props.store(testPropConfig.getProp()+testPropConfig.getFils().get("webDriver"));
        InputUtils.click(fraInterface,"[name=submit2]");
        System.out.println("index====end");

    }

    /**
     *
     * @param driver
     * @param meunNames 多级
     */
    public void chageMenu(WebDriver driver,String... meunNames){
        System.out.println("chageMenu====start");
        WebDriver fraMenu = IframeUtils.chageIframe(driver,"#fraMenu");
        WebElement webElement =null;
                String id=null;
        WebElement nextWebElement =null;
        for (int i=0;i<meunNames.length;i++){
            webElement = InputUtils.findByLinkText(fraMenu,meunNames[i]);
            id = webElement.getAttribute("id");
            id=id.replaceAll("link","tree");
            nextWebElement = InputUtils.find(fraMenu,"#"+id+">span:last-child");
            System.out.println(nextWebElement.isDisplayed()+"   "+ nextWebElement.getText());
            if(!nextWebElement.isDisplayed()||i==meunNames.length-1){
                webElement.click();
            }
            System.out.println( webElement.toString());
            System.out.println(webElement.getText());
        }
        System.out.println("chageMenu====end");
    }
    public void chageMenu(WebDriver browser,String meunUrl){
        System.out.println("chageMenu====start");
        WebDriver fraMenu = IframeUtils.chageIframe(browser,"#fraMenu");
//        ../app/NoScanContInput.jsp?type=1
        StringBuilder stringBuilder = new StringBuilder("a[href*='");
        stringBuilder.append(meunUrl).append("']");
        WaitUtils.wiatByCssSelector(fraMenu,stringBuilder.toString());
        InputUtils.click(fraMenu,stringBuilder.toString());
        System.out.println("chageMenu====end");
    }
    public void NoScanContInput(WebDriver browser){
        System.out.println("NoScanContInput====start");
        Props props = new Props(testPropConfig.getProp()+testPropConfig.getFils().get("tb"));
        String prtNo = props.getStr("PrtNo");
        String manageCom = props.getStr("ManageCom");
        WebDriver fraInterface = IframeUtils.chageIframe(browser,"#fraInterface");
        StringBuilder prtNoInupt = new StringBuilder("input[name='PrtNo']");
        WaitUtils.wiatByCssSelector(fraInterface,prtNoInupt.toString());
        InputUtils.setValue(fraInterface,prtNoInupt.toString(),prtNo);
        StringBuilder manageComInupt = new StringBuilder("input[name='ManageCom']");
        InputUtils.setValue(fraInterface,manageComInupt.toString(),manageCom);
        //查询
        JavascriptUtil.apply(fraInterface,"easyQueryClick()");
        WebElement webElement =  InputUtils.find(fraInterface,"#spanGrpGrid0");
        System.out.println(webElement.getText());
        if(webElement==null){
//            JavascriptUtil.apply(fraInterface,"ApplyInput()");
        }else{

        }
        System.out.println("NoScanContInput====end");
//

    }
}
