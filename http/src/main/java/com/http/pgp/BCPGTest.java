package com.http.pgp;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BCPGTest {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public BCPGTest() {
    }

    public static void main(String[] args) throws Exception {
        //加密
        //创建加密类
        Encrypt encrypt = new Encrypt();
        encrypt.setArmored(true);
        encrypt.setCheckIntegrity(true);
        //公钥路径
        encrypt.setPublicKeyFilePath("C:\\Users\\wangh\\Desktop\\public.asc");
        encrypt.setSigning(true);
        //私钥路径
        encrypt.setPrivateKeyFilePath("C:\\Users\\wangh\\Desktop\\private.gpg");
        //私钥密码
        encrypt.setPrivateKeyPassword("123456");
        BCPGPEncryptor bcpgpEnryptor = new BCPGPEncryptor(encrypt);
        //源文件地址
        String plainInputFile = "C:\\Users\\wangh\\Desktop\\a.txt";
        //加密后文件地址
        String encryptedOutputFile = "C:\\Users\\wangh\\Desktop\\a.txt.asc";
        //公钥路径
        String publicKeyFile = "C:\\Users\\wangh\\Desktop\\public.asc";
        //私钥路径
        String privateKeyFile = "C:\\Users\\wangh\\Desktop\\private.gpg";
        //私钥密码
        String passPhrase = "123456";
        //加密并签名方法
        bcpgpEnryptor.encryptAndSignFile(plainInputFile,encryptedOutputFile,publicKeyFile,privateKeyFile,passPhrase);
//        //解密
//        //创建解密类
//        Decrypt decrypt = new Decrypt();
//        //公钥路径
//        decrypt.setPublicKeyFilePath("C:\\Users\\wangh\\Desktop\\public.asc");
//        decrypt.setVerify(true);
//        //私钥路径
//        decrypt.setPrivateKeyFilePath("C:\\Users\\wangh\\Desktop\\private.gpg");
//        //私钥密码
//        decrypt.setPrivateKeyPassword("123456");
//        BCPGPDecryptor bcpgpDecryptor = new BCPGPDecryptor(decrypt);
//        //解密文件路径
//        String encryptedInputFile = "C:\\Users\\wangh\\Desktop\\a.txt.asc";
//        //解密后文件路径
//        String plainOutputFile = "C:\\Users\\wangh\\Desktop\\aa.txt";
//        //公钥路径
//        String publicKeyFile = "C:\\Users\\wangh\\Desktop\\public.asc";
//        //私钥路径
//        String privateKeyFile = "C:\\Users\\wangh\\Desktop\\private.gpg";
//        //密码
//        String passPhrase = "123456";
//        //验证并解密方法
//        bcpgpDecryptor.decryptandVerifyFile(encryptedInputFile,plainOutputFile,publicKeyFile,privateKeyFile,passPhrase);
    }
}
