package com.sinosoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by maruizhong on 2019/4/1.
 */
@RestController
@SpringBootApplication
public class testhttpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(testhttpsApplication.class,args);

    }
   @RequestMapping("/")

    public String hello() {
        return "hello wrold";
    }
}
