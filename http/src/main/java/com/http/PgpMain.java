package com.http;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PgpMain {

    public static void main(String[] args) {
        System.setProperty("javax.net.debug", "ssl,handshake");

        SpringApplication.run(PgpMain.class, args);
        System.out.print("-----------------启动成功---------------");
    }
}
