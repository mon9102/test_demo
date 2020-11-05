package com.testDemo.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @auther: zouren
 * @date: 2020/8/3 10:51
 * @description:
 */
public class MD5utils {
    public static final String encodeMD5(String inStr) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString().toUpperCase();
        } catch (Exception var7) {
            return null;
        }
    }

    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 字符串MD5加密字符串
     *
     * @param String
     *            arg
     * @return String
     */
    public static String encodeString(String arg) {
        if (arg == null) {
            arg = "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(arg.getBytes("UTF-8"));
        }
        catch (Exception e) {
        }
        return toHex(md5.digest());
    }

    /**
     * 16进制
     *
     * @param String
     *            bytes
     * @return String
     */
    private static String toHex(byte[] bytes) {
        StringBuffer str = new StringBuffer(32);
        for (byte b : bytes) {
            str.append(hexDigits[((b & 0xF0) >> 4)]);
            str.append(hexDigits[(b & 0xF)]);
        }
        return str.toString();
    }
    public static String getMd5(String str) {
        String s = str;
        if (str == null) {
            return "";
        } else {
            String value = null;
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var7) {
                var7.printStackTrace();
            }

            BASE64Encoder baseEncoder = new BASE64Encoder();

            try {
                value = baseEncoder.encode(md5.digest(s.getBytes("utf-8")));
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return value;
        }
    }
    public static void main(String[] args) throws Exception{
        BASE64Encoder baseEncoder = new BASE64Encoder();

        String key="aaaa";
        String charset="utf-8";
        System.out.println(encodeMD5(key));
        System.out.println(encodeString(key));
        System.out.println(getMd5(key));
        System.out.println(SecureUtil.md5().digestHex(key,charset));
        System.out.println(SecureUtil.md5(key));
        System.out.println(SecureUtil.hmacMd5().digestHex(key,charset));

    }
}
