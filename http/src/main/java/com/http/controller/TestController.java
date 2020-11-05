package com.http.controller;

import com.alibaba.fastjson.JSONObject;
import com.http.HttpClientforSSLConfig;
import com.http.pgp.BCPGPDecryptor;
import com.http.pgp.BCPGPEncryptor;
import com.http.pgp.Decrypt;
import com.http.pgp.Encrypt;
import com.http.util.PgpUtils;
import com.http.util.HttpClientforSSLInterface;
import io.swagger.annotations.*;

import java.io.*;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.openpgp.PGPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.security.NoSuchProviderException;
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
    @Value("${file1}")
    private String file1;
    @Value("${file2}")
    private String file2;
    @Value("${file3}")
    private String file3;
    @Value("${file5}")
    private String file5;
    @Value("${file7}")
    private String file7;
    @Value("${file9}")
    private String file9;
    //url
    @Value("${url1}")
    private String url1;
    @Value("${url2}")
    private String url2;
    @Value("${url3}")
    private String url3;
    @Value("${url5}")
    private String url5;
    @Value("${url9}")
    private String url9;

    @ApiOperation("test")
    @ApiParam(name = "num", value = ":56789", required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "测试号", paramType = "query", dataType = "String", example = "5,6,7,8,9", defaultValue = "5")
            ,
            @ApiImplicitParam(name = "gpg", value = "是否使用加密", paramType = "query", dataType = "String", example = "0,1", defaultValue = "0")
    })
    @RequestMapping("/{id}/{gpg}/{msgId}")
    public String test(@PathVariable String id, @PathVariable String gpg, @PathVariable String msgId) throws Exception {
        String keyId = "2c147fb2-1333-4c3d-8310-afada680f4b4";
        String param = null;
        String url = "eeeeee";
        msgId = StringUtils.isNotBlank(msgId) ? msgId : "20190410HK001";

        Map<String, String> header = new HashMap<String, String>();
        header.put("msgId", msgId);
        header.put("orgId", "HKALICL");
        header.put("ORG_ID", "HKALICL");//校验是使用
        header.put("keyId", keyId);
        LocalDateTime Idt4 = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timeStamp = Idt4.format(formatter).replaceAll(" ", "T");


        header.put("timeStamp", timeStamp);


        switch (id) {
            case "1":

                header.put("keyId", "c6e020e9-1e6d-4757-afd9-45a801e22778");
                //effDate 设置为明天
                param = param1(gpg, header);
                url = url1;
                break;
            case "2":
                header.put("keyId", "de8b9aef-f6f3-4dee-8383-1cb5e92c6c2a");

                param = param2(gpg, header);
                url = url2;
                break;
            case "3":
                param = param3(gpg, header);
                url = url3;
                break;
            case "6":
                param = param6(gpg);
                url = url5;
                break;
            case "7":
                param = param7(gpg, header);
                url = url5;
                break;
            case "8":
                param = param8(gpg);
                url = url5;
                break;
            case "9":
                keyId = "de8b9aef-f6f3-4dee-8383-1cb5e92c6c2a";
                header.put("keyId", keyId);


//                header.put("orgId", "HKSYHY");
                param = paramverification(gpg, header);
                url = url9;
                break;
            case "verification":
                keyId = "de8b9aef-f6f3-4dee-8383-1cb5e92c6c2a";
                header.put("keyId", keyId);
//                header.put("orgId", "HKSYHY");
                param = paramverification(gpg, header);
                url = url9;

                break;
            default:
                param = param5(gpg, header);
                url = url5;
                break;

        }
//        return param;
        HttpClientforSSLInterface https = httpConfig.getHttpClientforSSL(dual);
        https.init(keyStoreFile, keyStorePass, trustStoreFile, trustStorePass);
        String re = https.post(url, header, param);
        log.info("请求成功返回::" + re);
        File f5 = new File(file5);
        File reFlie = new File(f5.getParent() + "re_ENCRYPT_Emp.asc");
        FileUtils.write(reFlie, re, Charset.defaultCharset());
        re = reNewPGP(reFlie);
        return re;
    }

    private String paramverification(String gpg, Map<String, String> header) {
        return pgp(file9, gpg, header);
    }

    /**
     * @Description: 服文5请求
     * @Param: gpg 1为使用gpg加密
     * @Return: java.util.Map<java.lang.String, java.lang.String>
     * @CreateDate: 2019/4/1 11:21
     */
    
    private String param1(String gpg, Map<String, String> header) {
        return pgp(file1, gpg, header);
    }
    
    private String param2(String gpg, Map<String, String> header) {
        return pgp(file2, gpg, header);
    }
    
    private String param3(String gpg, Map<String, String> header) {
        return pgp(file3, gpg, header);
    }
    
    private String param5(String gpg, Map<String, String> header) {
        return pgp(file5, gpg, header);
    }

    private String param6(String gpg) {
        return null;
    }

    private String param7(String gpg, Map<String, String> header) {
        return pgp(file7, gpg, header);
    }

    private String param8(String gpg) {
        return null;
    }



    private String pgp(String fileName, String gpg, Map<String, String> header) {
        log.info("request header::" + header.toString());
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
                testheader.put("timeStamp", header.getOrDefault("timeStamp", ""));
                testheader.put("msgId", header.getOrDefault("msgId", ""));
                testheader.put("orgId", header.getOrDefault("orgId", ""));
                input1 = test.toString();
                FileUtils.write(new File(fileName), input1, Charset.defaultCharset());
                input = newPGP(input1, paramFile);

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return input;

    }

    private String newPGP(String input1, File paramFile) throws Exception {
        log.info("newPGP请求体加密前::" + input1);
        //加密
        //创建加密类
        Encrypt encrypt = new Encrypt();
        encrypt.setArmored(true);
        encrypt.setCheckIntegrity(true);
        //公钥路径
        encrypt.setPublicKeyFilePath(publicKey);
        encrypt.setSigning(true);
        //私钥路径
        encrypt.setPrivateKeyFilePath(privateKey);
        //私钥密码
        encrypt.setPrivateKeyPassword("HuvHGF0932weBM766");
        BCPGPEncryptor bcpgpEnryptor = new BCPGPEncryptor(encrypt);
        //源文件地址
        String plainInputFile = paramFile.getPath();
        //加密后文件地址
        String encryptedOutputFile = paramFile.getParent() + "ENCRYPT" + paramFile.getName();
        //公钥路径
        String publicKeyFile = publicKey;
        //私钥路径
        String privateKeyFile = privateKey;
        //私钥密码
        String passPhrase = "HuvHGF0932weBM766";
        //加密并签名方法
        bcpgpEnryptor.encryptAndSignFile(plainInputFile, encryptedOutputFile, publicKeyFile, privateKeyFile, passPhrase);
        File encryptFile = new File(encryptedOutputFile);

        input1 = FileUtils.readFileToString(encryptFile);
        log.info("newPGP请求体加密后进行签名::" + input1);
        return input1;
    }

    private String oldPGP(String input1, File paramFile) throws Exception {
        log.info("请求体加密前::" + input1);
        File keyInFile = new File(publicKey);
        FileInputStream keyIn = new FileInputStream(keyInFile);

        FileOutputStream outputFile = new FileOutputStream(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
        pgu.encryptFile(outputFile, paramFile.getPath(), pgu.readPublicKey(keyIn), true, true);
        File encryptFile = new File(paramFile.getParent() + "ENCRYPT" + paramFile.getName());
        PgpUtils.signatureCreate(encryptFile.getAbsolutePath(), privateKey, paramFile.getParent() + "SIGNATURE" + paramFile.getName(), "HuvHGF0932weBM766");
        File signatureFile = new File(paramFile.getParent() + "SIGNATURE" + paramFile.getName());
        log.info("请求体加密后::" + FileUtils.readFileToString(encryptFile));
        input1 = FileUtils.readFileToString(signatureFile);
        log.info("请求体加密后进行签名::" + input1);
        return input1;
    }

    private String reNewPGP(File reFlie) throws IOException, PGPException, NoSuchProviderException {
        //解密
        //创建解密类
        Decrypt decrypt = new Decrypt();
        //公钥路径
        decrypt.setPublicKeyFilePath(publicKey);
        decrypt.setVerify(true);
        //私钥路径
        decrypt.setPrivateKeyFilePath(privateKey);
        //私钥密码
        decrypt.setPrivateKeyPassword("HuvHGF0932weBM766");
        BCPGPDecryptor bcpgpDecryptor = new BCPGPDecryptor(decrypt);
        //解密文件路径
        String encryptedInputFile = reFlie.getPath();
        //解密后文件路径
        String plainOutputFile = reFlie.getParent() + "re_ENCRYPT_Emp.txt";
        //公钥路径
        String publicKeyFile = publicKey;
        //私钥路径
        String privateKeyFile = privateKey;
        //密码
        String passPhrase = "HuvHGF0932weBM766";
        //验证并解密方法
        bcpgpDecryptor.decryptandVerifyFile(encryptedInputFile, plainOutputFile, publicKeyFile, privateKeyFile, passPhrase);
        File reFile = new File(plainOutputFile);
        String re = FileUtils.readFileToString(reFile);
        log.info("返回体解密::" + re);
        return re;
    }

    public static void main(String[] args) {

    }
}
