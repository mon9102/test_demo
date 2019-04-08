package com.sinosoft;

import com.sinosoft.util.PgpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PgpTest {
    public static void main(String[] args) {
        //生成签名文件
        //被签名文件路径
        String filePath = "C:\\Users\\wangh\\Desktop\\testVerify.txt";
        //私钥路径
        String privateKeyPath = "C:\\Users\\wangh\\Desktop\\private.gpg";
        //输出证书路径（签名文件路径）
        String outFilePath = "C:\\Users\\wangh\\Desktop\\testVerify.txt.asc";
        //私钥密码
        String passWord = "123456";
        PgpUtils.signatureCreate(filePath,privateKeyPath,outFilePath,passWord);

//        //校验签名文件
//        //被签名文件路径（即13行的被签名文件）
//        String filePath = "C:\\Users\\wangh\\Desktop\\testVerify.txt";
//        //公钥路径
//        String publicKeyPath = "C:\\Users\\wangh\\Desktop\\public.asc";
//        //签名文件路径(17行生成的签名文件)
//        String signFilePath = "C:\\Users\\wangh\\Desktop\\testVerify.txt.asc";
//        System.out.println(PgpUtils.verifySignature(filePath,publicKeyPath,signFilePath));
    }
}
