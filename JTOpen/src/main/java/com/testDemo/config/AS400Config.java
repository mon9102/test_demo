package com.testDemo.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zouren
 * @Date: 2019/5/22 11:31
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "as400")
@Getter
@Setter
public class AS400Config {
    private String systemName ;
    private String username;
    private String password;
    private String jobName;
    private String jobNumber;
    private String jobuser;
}
