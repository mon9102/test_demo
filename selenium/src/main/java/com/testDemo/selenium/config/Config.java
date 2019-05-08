package com.testDemo.selenium.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: zouren
 * @Date: 2019/5/8 11:32
 * @Description:
 */

@Component
public class Config implements InitializingBean {
    @Value("${webDriver.chrome}")
    private String  chromeWebDriver;
    @Value("${webDriver.ie}")
    private String  ieWebDriver;
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(chromeWebDriver);
        System.out.println(ieWebDriver);
        System.setProperty("webdriver.chrome.driver",chromeWebDriver);
        System.setProperty("webdriver.ie.driver",ieWebDriver);

    }
}
