package com.testDemo.selenium.webDriver;

import com.testDemo.selenium.utils.IframeUtils;
import com.testDemo.selenium.utils.InputUtils;
import com.testDemo.selenium.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.springframework.stereotype.Component;

import java.net.URL;

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


    public void index(String url,String username,String password){
        indexUrl = url;
        InternetExplorerDriver webDriver=getInternetExplorerDriver();
        WebDriver fraInterface = IframeUtils.chageIframe(webDriver,"#fraInterface");
        WaitUtils.wiatByCssSelector(fraInterface,"#UserCode2");
        InputUtils.setValue(fraInterface,"#UserCode2",username);
        InputUtils.setValue(fraInterface,"#PWD2",password);
        InputUtils.setValue(fraInterface,"#PWD2",password);

        InputUtils.click(fraInterface,"[name=submit2]");

    }

    public void chageMenu(WebDriver browser,String meunUrl){

        WebDriver fraMenu = IframeUtils.chageIframe(browser,"#fraMenu");
//        ../app/NoScanContInput.jsp?type=1
        StringBuilder stringBuilder = new StringBuilder("[href=");
        stringBuilder.append(meunUrl).append("]");
        WaitUtils.wiatByCssSelector(fraMenu,stringBuilder.toString());
        InputUtils.click(fraMenu,stringBuilder.toString());
    }

}
