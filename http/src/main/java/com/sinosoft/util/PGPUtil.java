package com.sinosoft.util;



import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cryptix.jce.provider.CryptixCrypto;
import cryptix.message.EncryptedMessage;
import cryptix.message.EncryptedMessageBuilder;
import cryptix.message.KeyBundleMessage;
import cryptix.message.LiteralMessage;
import cryptix.message.LiteralMessageBuilder;
import cryptix.message.Message;
import cryptix.message.MessageException;
import cryptix.message.MessageFactory;
import cryptix.message.NotEncryptedToParameterException;
import cryptix.message.SignedMessage;
import cryptix.message.SignedMessageBuilder;
import cryptix.openpgp.PGPArmouredMessage;
import cryptix.openpgp.provider.CryptixOpenPGP;
import cryptix.pki.KeyBundle;
import org.apache.commons.codec.binary.Base64;

public class PGPUtil {
    /**
     * 添加提供者
     */
    static{
        Security.addProvider(new CryptixCrypto());
        Security.addProvider(new CryptixOpenPGP() );
    }

    /**
     * 构建 LiteralMessage 对象
     * @param message
     * @return
     * @throws MessageException
     */
    private static LiteralMessage buildLiteralMessage(byte[] message) throws MessageException{
        LiteralMessageBuilder lmb = null;

        try {
            lmb = LiteralMessageBuilder.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        lmb.init(message);
        LiteralMessage literal = (LiteralMessage)lmb.build();
        return literal;
    }

    /**
     * 使用多个公钥对明文加密
     * @param plain			明文
     * @param recipientKeys	公钥集合
     * @return				加密后的明文
     * @throws MessageException
     */
    public static byte[] encrypt(byte[] plain, List<KeyBundle> recipientKeys) throws MessageException{
        LiteralMessage literal = buildLiteralMessage(plain);

        EncryptedMessageBuilder emb = null;
        try {
            emb = EncryptedMessageBuilder.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        emb.init(literal);

        //添加接受者
        for(KeyBundle key : recipientKeys){
            emb.addRecipient(key);
        }
        //压缩
        emb.setAttribute("compressed", "true");
        //得到加密信息
        Message msg = emb.build();
        PGPArmouredMessage pgpMsg = new PGPArmouredMessage(msg);
        return pgpMsg.getEncoded();
    }

    /**
     * 使用单张公钥加密
     * @param plain		明文
     * @param publicKey	公钥
     * @return	返回加密后的密文
     * @throws MessageException
     */
    public static byte[] encrypt(byte[] plain,KeyBundle publicKey) throws MessageException{
        List<KeyBundle> list = new ArrayList<KeyBundle>();
        list.add(publicKey);
        return encrypt(plain, list);
    }

    /**
     * 使用单张公钥加密
     * @param plain		明文
     * @param publicKey	公钥路径
     * @return	返回加密后的密文
     * @throws MessageException
     */
    public static byte[] encrypt(byte[] plain,String publicKeyPath) throws MessageException, IOException {
        InputStream priInputStream  = new FileInputStream(new File(publicKeyPath));
        KeyBundle publicKeyBundle = PGPUtil.streamToKeyBundle(priInputStream);
        return PGPUtil.encrypt(plain, publicKeyBundle);
    }

    /**
     * 使用私钥和密码对明文签名
     * @param plain			明文
     * @param privateKey	私钥
     * @param keypass		私钥密码
     * @return				签名后的明文
     * @throws MessageException
     * @throws UnrecoverableKeyException
     */
    public static byte[] sign(byte[] plain,KeyBundle privateKey,String keypass)throws MessageException, UnrecoverableKeyException {
        SignedMessageBuilder smb = null;
        try {
            smb = SignedMessageBuilder.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        LiteralMessage literal = buildLiteralMessage(plain);
        smb.init(literal);
        smb.addSigner(privateKey, keypass.toCharArray());

        Message msg = smb.build();
        PGPArmouredMessage armoured = new PGPArmouredMessage(msg);
        return armoured.getEncoded();
    }

    /**
     * 使用私钥和密码对明文签名
     * @param plain			明文
     * @param privateKey	私钥路径
     * @param keypass		私钥密码
     * @return				签名后的明文
     * @throws MessageException
     * @throws UnrecoverableKeyException
     */
    public static byte[] sign(byte[] plain,String privateKeyPath,String keypass)throws MessageException,UnrecoverableKeyException, IOException{
        InputStream priInputStream  = new FileInputStream(new File(privateKeyPath));
        KeyBundle privateKeyBundle = PGPUtil.streamToKeyBundle(priInputStream);
        return PGPUtil.sign(plain, privateKeyBundle, keypass);
    }

    /**
     * 使用私钥和密码解密加密后的数据
     * @param encrypted		PGP加密过的数据
     * @param privateKey	私钥
     * @param keypass		私钥密码
     * @return				解密后的明文
     * @throws MessageException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws NotEncryptedToParameterException
     */
    public static byte[] decrypt(byte[] encrypted,KeyBundle privateKey,String keypass) throws MessageException, IOException, UnrecoverableKeyException, NotEncryptedToParameterException{

        MessageFactory mf = null;
        try {
            mf = MessageFactory.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Collection msgs = mf.generateMessages(new ByteArrayInputStream(encrypted));

        //得到集合中的EncryptedMessage对象
        Message message = (Message)msgs.iterator().next();

        if (!(message instanceof EncryptedMessage)) {
            throw new MessageException("Not a encrypted message.");
        }

        EncryptedMessage em = (EncryptedMessage)message;
        Message msg = em.decrypt(privateKey,keypass.toCharArray());
        return ((LiteralMessage)msg).getBinaryData();
    }

    /**
     * 使用私钥和密码解密加密后的数据
     * @param encrypted		PGP加密过的数据
     * @param privateKey	私钥路径
     * @param keypass		私钥密码
     * @return				解密后的明文
     * @throws MessageException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws NotEncryptedToParameterException
     */
    public static byte[] decrypt(byte[] encrypted,String privateKeyPath,String keypass) throws MessageException, IOException, UnrecoverableKeyException, NotEncryptedToParameterException{
        InputStream priInputStream  = new FileInputStream(new File(privateKeyPath));
        KeyBundle privateKeyBundle = PGPUtil.streamToKeyBundle(priInputStream);
        return PGPUtil.decrypt(encrypted,privateKeyBundle,keypass);
    }

    /**
     * 解密验签
     * @param encrypted		密文
     * @param privateKey	私钥
     * @param keypass		私钥密码
     * @param publicKey		公钥
     * @return				返回明文
     * @throws UnrecoverableKeyException
     * @throws MessageException
     * @throws IOException
     * @throws NotEncryptedToParameterException
     */
    public static byte[] decryptVerify(byte[] encrypted,KeyBundle privateKey,String keypass,KeyBundle publicKey) throws UnrecoverableKeyException, MessageException, IOException, NotEncryptedToParameterException{
        return PGPUtil.verify(PGPUtil.decrypt(encrypted, privateKey, keypass), publicKey);
    }

    /**
     * 解密验签
     * @param encrypted		密文
     * @param privateKey	私钥路径
     * @param keypass		私钥密码
     * @param publicKey		公钥路径
     * @return				返回明文
     * @throws UnrecoverableKeyException
     * @throws MessageException
     * @throws IOException
     * @throws NotEncryptedToParameterException
     */
    public static byte[] decryptVerify(byte[] encrypted,String privateKeyPath,String keypass,String publicKeyPath) throws UnrecoverableKeyException, MessageException, IOException, NotEncryptedToParameterException{
        InputStream priInputStream  = new FileInputStream(new File(privateKeyPath));
        KeyBundle privateKeyBundle = PGPUtil.streamToKeyBundle(priInputStream);

        InputStream pubInputStream = new FileInputStream(new File(publicKeyPath));
        KeyBundle pubKeyBundle = PGPUtil.streamToKeyBundle(pubInputStream);

        return PGPUtil.decryptVerify(encrypted,privateKeyBundle,keypass,pubKeyBundle);
    }

    /**
     * 验证Message
     * @param signed	验证的内容
     * @param publickey	公钥
     * @return			返回验证后的内容
     * @throws MessageException
     * @throws IOException
     */
    public static byte[] verify(byte[] signed,KeyBundle publickey) throws MessageException, IOException{

        MessageFactory mf = null;
        try {
            mf = MessageFactory.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Message msg = (Message)mf.generateMessages(new ByteArrayInputStream(signed)).iterator().next();
        if (!(msg instanceof SignedMessage)) {
            throw new MessageException(" Not a signed message.");
        }

        SignedMessage sm = (SignedMessage)msg;
        if (sm.verify(publickey)) {

        } else {
            throw new MessageException(" Signature verify fail. ");
        }

        if (!(sm.getContents() instanceof LiteralMessage)){
            throw new MessageException(" Not a signed message.");
        }

        LiteralMessage lm = (LiteralMessage)sm.getContents();
        return lm.getBinaryData();
    }

    /**
     * 流转换为PGP KeuBundle 对象
     * @param inputStream 	Key
     * @return	转换后的 KeuBundle
     * @throws MessageException
     * @throws IOException
     */
    public static KeyBundle streamToKeyBundle(InputStream inputStream) throws MessageException, IOException {
        MessageFactory messageFactory = null;
        try {
            messageFactory = MessageFactory.getInstance("OpenPGP");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Collection msgs = messageFactory.generateMessages(inputStream);
        KeyBundleMessage keybm = (KeyBundleMessage)msgs.iterator().next();

        return keybm.getKeyBundle();

    }

    /**
     * 签名加密
     * @param plain			明文
     * @param privateKey	私钥
     * @param keypass		私钥密码
     * @param recipientKeys	公钥
     * @return				返回签名加密后的数据
     * @throws UnrecoverableKeyException
     * @throws MessageException
     */
    public static byte [] signAndEncrypt(byte[] plain,KeyBundle privateKey,String keypass,List<KeyBundle> recipientKeys) throws UnrecoverableKeyException, MessageException{
        return PGPUtil.encrypt(PGPUtil.sign(plain, privateKey, keypass),recipientKeys);
    }

    /**
     * 签名加密
     * @param plain			明文
     * @param privateKey	私钥
     * @param keypass		私钥密码
     * @param recipientKeys	公钥
     * @return				返回签名加密后的数据
     * @throws UnrecoverableKeyException
     * @throws MessageException
     */
    public static byte [] signAndEncrypt(byte[] plain,KeyBundle privateKey,String keypass,KeyBundle publicKey) throws UnrecoverableKeyException, MessageException{
        return PGPUtil.encrypt(PGPUtil.sign(plain, privateKey, keypass),publicKey);
    }

    /**
     * @param plain                 明文
     * @param privateKeyPath        私钥路径
     * @param keypass               私钥密码
     * @param publicKeyPath         公钥
     * @return                      返回签名加密后的数据
     * @throws UnrecoverableKeyException
     * @throws MessageException
     * @throws IOException
     */
    public static byte [] signAndEncrypt(byte[] plain,String privateKeyPath,String keypass,String publicKeyPath) throws UnrecoverableKeyException, MessageException, IOException {
        InputStream priInputStream  = new FileInputStream(new File(privateKeyPath));
        KeyBundle privateKeyBundle = PGPUtil.streamToKeyBundle(priInputStream);

        InputStream pubInputStream = new FileInputStream(new File(publicKeyPath));
        KeyBundle pubKeyBundle = PGPUtil.streamToKeyBundle(pubInputStream);

        return PGPUtil.signAndEncrypt(plain,privateKeyBundle,keypass,pubKeyBundle);
    }

    public static void main(String[] args) throws MessageException, IOException, UnrecoverableKeyException, NotEncryptedToParameterException {
        //签名私钥
        String privateKeyPath = "C:\\Users\\wangh\\Desktop\\private.gpg";
        //私钥密码
        String keypass = "123456";
        //加密公钥
        String publicKeyPath = "C:\\Users\\wangh\\Desktop\\public.asc";
        //加密文本
        byte[] a = "aaaa".getBytes();
        System.out.println(a);
        //签名后返回
        byte[] b = sign(a,privateKeyPath,keypass);
        System.out.println(Arrays.toString(b));
        InputStream pubInputStream = new FileInputStream(new File(privateKeyPath));
        KeyBundle pubKeyBundle = PGPUtil.streamToKeyBundle(pubInputStream);
        //验证（用公钥就报错，很奇怪）
        System.out.println(Arrays.toString(verify(b,pubKeyBundle)));


    }
}
