package com.testDemo.selenium.webDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 14:36
 * @Description:
 */
@Component
public class IeDriverLis {
    private InternetExplorerDriver internetExplorerDriver;
    private String indexUrl = null;
    public InternetExplorerDriver getInternetExplorerDriver(){
        if(internetExplorerDriver==null){
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, indexUrl);
            internetExplorerDriver =new InternetExplorerDriver(internetExplorerOptions);
            String sessionid = internetExplorerDriver.getSessionId().toString();
            System.out.println("session ID --->" + sessionid);

            URL url = ((HttpCommandExecutor)(internetExplorerDriver.getCommandExecutor())).getAddressOfRemoteServer();
            System.out.println("url --->" + url.toString());

        }
        return internetExplorerDriver;
    }

    /**
     * 切换到某个iframe里
     * @param browser
     * @param iframeCssSelector
     * @return
     */
    public WebDriver chageIframe(WebDriver browser,String iframeCssSelector){
       return browser.switchTo().frame(browser.findElement(By.cssSelector(iframeCssSelector)));
    }

    /**加入有多层iframe的回到上一层
     *
     * @param browser
     * @return
     */
    public WebDriver parenIframe(WebDriver browser ){
        return browser.switchTo().parentFrame();
    }
    /**
     *  回到主文档
     * @param browser
     * @return
     */
    public WebDriver topPage(WebDriver browser ){
        return browser.switchTo().defaultContent();
    }

    public void index(String url,String username,String password){
        indexUrl = url;
        InternetExplorerDriver webDriver=getInternetExplorerDriver();
        WebDriver fraInterface = chageIframe(webDriver,"#fraInterface");
        WebDriverWaitUtils.wiatByCssSelector(fraInterface,"#UserCode2");
        WebDriverInputUtils.setValue(fraInterface,"#UserCode2",username);
        WebDriverInputUtils.setValue(fraInterface,"#PWD2",password);
        WebDriverInputUtils.setValue(fraInterface,"#PWD2",password);

        WebDriverInputUtils.click(fraInterface,"[name=submit2]");

    }

    public void chageMenu(WebDriver browser,String meunUrl){

        WebDriver fraMenu = chageIframe(browser,"#fraMenu");
//        ../app/NoScanContInput.jsp?type=1
        StringBuilder stringBuilder = new StringBuilder("[href=");
        stringBuilder.append(meunUrl).append("]");
        WebDriverWaitUtils.wiatByCssSelector(fraMenu,stringBuilder.toString());
        WebDriverInputUtils.click(fraMenu,stringBuilder.toString());
    }

}
