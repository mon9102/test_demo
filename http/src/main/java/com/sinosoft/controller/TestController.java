package com.sinosoft.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.util.HttpClientforSSL;
import com.sinosoft.util.PgpUtils;
import io.swagger.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Value("${keyStore}")
    private String keyStore;
    @Value("${passWord}")
    private String passWord;
    @Autowired
    HttpClientforSSL https;
    //gpg
    @Autowired
    PgpUtils pgu;
    @Value("${publicKey}")
    private String publicKey;
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
    @RequestMapping("/{id}/{gpg}")
    public String test(@PathVariable String id, @PathVariable String gpg) throws Exception {
        String param = null;
        String url = "eeeeee";
        
        
        Map<String, String> header = new HashMap<String, String>();
        header.put("msgId", "SG70123987456");
        header.put("orgId", "0123");
        header.put("timeStamp", "2017-01-26T16:16:43.567");

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
                param = param5(gpg);
                url = url5;
                break;

        }
        https.init(keyStore, passWord);
        String re = https.post(url, header, param);
        return re;
    }

    /**
     * @Description: 服文5请求
     * @Param: gpg 1为使用gpg加密
     * @Return: java.util.Map<java.lang.String,java.lang.String>
     * @CreateDate: 2019/4/1 11:21
     */
    private String param5(String gpg) {
        return pgp(file5, gpg);
    }

    private String param6(String gpg) {
        return pgp(file5, gpg);
    }

    private String param7(String gpg) {
        return pgp(file5, gpg);
    }

    private String param8(String gpg) {
        return pgp(file5, gpg);
    }

    private String param9(String gpg) {
        return pgp(file5, gpg);
    }

    private String pgp(String fileName, String gpg) {
        String input = "e";
        File paramFile = new File(fileName);
        try {
            if (gpg.equalsIgnoreCase("0")) {
                input = FileUtils.readFileToString(paramFile);
            }
            if (gpg.equalsIgnoreCase("1")) {
                String input1 = FileUtils.readFileToString(paramFile);
                log.info("请求报文.........."+input1);
                File keyInFile = new File(publicKey);
                FileInputStream keyIn = new FileInputStream(keyInFile);
                FileOutputStream outputFile = new FileOutputStream(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
                pgu.encryptFile(outputFile, fileName, pgu.readPublicKey(keyIn), true, true);
                File encryptFile = new File(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
                log.info("-----------" + encryptFile.getAbsolutePath() + "-----------");
                input = FileUtils.readFileToString(encryptFile);
                //input = input.substring(49, input.length()-27);
                log.info("加密的报文"+input);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return input;

    }

}
