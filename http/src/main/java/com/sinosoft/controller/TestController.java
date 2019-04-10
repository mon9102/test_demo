package com.sinosoft.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.HttpClientforSSLConfig;
import com.sinosoft.util.HttpClientforSSLInterface;
import com.sinosoft.util.HttpClientforSSLOne;
import com.sinosoft.util.PgpUtils;
import io.swagger.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by zoure on 2018/5/14.
 */
@Api(value = "test", description = "test", tags = "tag1")
@RestController
@RequestMapping("/test")
public class TestController {

    private Logger log = LoggerFactory.getLogger(getClass());

    //ssl
    @Value("${keyStoreFile}")
    private String keyStoreFile;
    @Value("${keyStorePass}")
    private String keyStorePass;
    @Value("${trustStoreFile}")
    private String trustStoreFile;
    @Value("${trustStorePass}")
    private String trustStorePass;
    @Value("${Dual}")
    private String dual;
    @Autowired
    HttpClientforSSLConfig httpConfig;
    //gpg
    @Autowired
    PgpUtils pgu;
    @Value("${publicKey}")
    private String publicKey;
    @Value("${privateKey}")
    private String privateKey;
    @Value("${file5}")
    private String file5;
    //url
    @Value("${url5}")
    private String url5;

    
    
    
    @ApiOperation("test")
    @ApiParam(name = "num", value = ":56789", required = true)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "测试号", paramType = "query", dataType = "String", example = "5,6,7,8,9", defaultValue = "5")
        ,
            @ApiImplicitParam(name = "gpg", value = "是否使用加密", paramType = "query", dataType = "String", example = "0,1", defaultValue = "0")
    })
    @RequestMapping("/{id}/{gpg}/{msgId}")
    public String test(@PathVariable String id, @PathVariable String gpg,@PathVariable String msgId) throws Exception {
        String param = null;
        String url = "eeeeee";
        msgId = StringUtils.isNotBlank(msgId)?msgId:"20190410HK001";
        
        Map<String, String> header = new HashMap<String, String>();
        header.put("msgId", msgId);
        header.put("orgId", "HKALICL");
        header.put("keyId", "2c147fb2-1333-4c3d-8310-afada680f4b4");
        LocalDateTime Idt4= LocalDateTime.now();

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timeStamp = Idt4.format(formatter).replaceAll(" ", "T");



        header.put("timeStamp", timeStamp);
        log.info("request header::"+header.toString());

        switch (id) {
            case "6":
                param = param6(gpg);
                url = url5;
                break;
            case "7":
                param = param7(gpg);
                url = url5;
                break;
            case "8":
                param = param8(gpg);
                url = url5;
                break;
            case "9":
                param = param9(gpg);
                url = url5;
                break;
            default:
                param = param5(gpg,header);
                url = url5;
                break;

        }
//        return param;
        HttpClientforSSLInterface https = httpConfig.getHttpClientforSSL(dual);
        https.init(keyStoreFile, keyStorePass, trustStoreFile, trustStorePass);
        String re = https.post(url, header, param);
        return re;
    }

    /**
     * @Description: 服文5请求
     * @Param: gpg 1为使用gpg加密
     * @Return: java.util.Map<java.lang.String,java.lang.String>
     * @CreateDate: 2019/4/1 11:21
     */
    private String param5(String gpg,Map<String, String> header) {
        return pgp(file5, gpg, header);
    }

    private String param6(String gpg) {
        return null;
    }

    private String param7(String gpg) {
        return null;
    }

    private String param8(String gpg) {
        return null;
    }

    private String param9(String gpg) {
        return null;
    }

    private String pgp(String fileName, String gpg,Map<String, String> header) {
        String input = "e";
        File paramFile = new File(fileName);
        try {
            if (gpg.equalsIgnoreCase("0")) {
                input = FileUtils.readFileToString(paramFile);
            }
            if (gpg.equalsIgnoreCase("1")) {
                String input1 = FileUtils.readFileToString(paramFile);
                JSONObject test = JSONObject.parseObject(input1);

                JSONObject testheader = test.getJSONObject("header");
                testheader.put("timeStamp", header.getOrDefault("timeStamp",""));
                testheader.put("msgId", header.getOrDefault("msgId",""));

                input1 = test.toString();
                log.info("请求体加密前::"+input1);
                FileUtils.write(new File(fileName),input1, Charset.defaultCharset());
                File keyInFile = new File(publicKey);
                FileInputStream keyIn = new FileInputStream(keyInFile);
                FileOutputStream outputFile = new FileOutputStream(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
                pgu.encryptFile(outputFile, fileName, pgu.readPublicKey(keyIn), true, true);
                File encryptFile = new File(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
//                log.info("-----------" + encryptFile.getAbsolutePath() + "-----------");
                PgpUtils.signatureCreate(encryptFile.getAbsolutePath(),privateKey,paramFile.getParent() + "SIGNATURE" + paramFile.getName(),"HuvHGF0932weBM766");
                File signatureFile = new File(paramFile.getParent() + "SIGNATURE" + paramFile.getName());
                //input = FileUtils.readFileToString(encryptFile);
                log.info("请求体加密后::"+FileUtils.readFileToString(encryptFile));
                input = FileUtils.readFileToString(signatureFile);
                log.info("请求体加密后进行签名::"+input);
                //input = input.substring(49, input.length()-27);
//                log.info("加密的报文"+input);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return input;

    }

}
