package com.testDemo.selenium.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: zouren
 * @Date: 2019/5/9 11:09
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "testconfig")
public class TestPropConfig {
    private String prop;
    private Map<String,String> fils;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public Map<String, String> getFils() {
        return fils;
    }

    public void setFils(Map<String, String> fils) {
        this.fils = fils;
    }
}
