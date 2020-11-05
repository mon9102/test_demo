package com.https;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by maruizhong on 2019/4/1.
 */
@RestController
@SpringBootApplication
public class testhttpsApplication {
    public static void main(String[] args) {
        System.setProperty("javax.net.debug","ssl");
        SpringApplication.run(testhttpsApplication.class,args);

    }
  @RequestMapping("/")

    public String hello() {
        return "hello wrold";
    }
    @RequestMapping(value = "/getRequestInfo",method= {RequestMethod.POST,RequestMethod.GET})
    public String getRequestInfo(HttpServletRequest request) throws IOException {
        System.out.println("herader.msgId....."+ request.getHeader("msgId"));
        System.out.println("herader.orgId....."+ request.getHeader("orgId"));
        System.out.println("herader.timeStamp....."+ request.getHeader("timeStamp"));
//        System.out.println("getParameter.aa....."+ request.getParameter("aa"));
//        System.out.println("getParameterMap....."+ request.getParameterMap());
        System.out.println("getAuthType....."+ request.getAuthType());
        System.out.println("getContextPath()....."+ request.getContextPath());
        System.out.println("getMethod()....."+ request.getMethod());
        System.out.println("getPathInfo....."+ request.getPathInfo());
        System.out.println("getPathTranslated....."+ request.getPathTranslated());
        System.out.println("getQueryString....."+ request.getQueryString());
        System.out.println("getRemoteUser....."+ request.getRemoteUser());
        System.out.println("getRequestedSessionId....."+ request.getRequestedSessionId());
        System.out.println("getRequestURI....."+ request.getRequestURI());
        System.out.println("getServletPath....."+ request.getServletPath());
        System.out.println("getCharacterEncoding....."+ request.getCharacterEncoding());
        System.out.println("getContentType....."+ request.getContentType());
        System.out.println("getProtocol....."+ request.getProtocol());
        System.out.println("getRemoteAddr....."+ request.getRemoteAddr());
        System.out.println("getRemoteHost....."+ request.getRemoteHost());
        System.out.println("getScheme....."+ request.getScheme());
        System.out.println("getServerName....."+ request.getServerName());
//        String str="";
        InputStream in = request.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bd = new BufferedReader(reader);
        String inputLine = "";
        while ((inputLine = bd.readLine())!=null){
            System.out.println(inputLine);
        }
        return "Hello";
    }
}
