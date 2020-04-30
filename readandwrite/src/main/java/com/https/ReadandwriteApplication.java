package com.https;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.sinosoft.demo2.mapper")
@SpringBootApplication
public class ReadandwriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadandwriteApplication.class, args);
    }

}
