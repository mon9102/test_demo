package com.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.http.util.PgpUtils;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Test
    public void test(){
        JSONObject object = new JSONObject();
        JSONObject aa = new JSONObject();
        aa.put("eee","eee11");
        object.put("aa",aa);
        object.put("eee","eeee11");
        object.put("eee22","eeee22");
        object.put("bbb","eeee11");
        System.out.println("未忽略字段前:"+object.toString());//未忽略前

         SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
         filter.getExcludes().add("eee");
        System.out.println("忽略字段后:"+ JSON.toJSONString(object,filter));
        String regex = "\"\\w+\":";
        System.out.println(getMatcher(regex,object.toString()));
    }
    public static String getMatcher(String regex, String source) {
                 String result = "";
                 Pattern pattern = Pattern.compile(regex);
                 Matcher matcher = pattern.matcher(source);
                 while (matcher.find()) {
                     System.out.println(matcher.group()+"   matcher.start=="+matcher.start()+" matcher.end=="+matcher.end()+"    key="+source.substring(matcher.start()+1,matcher.end()-2));
                    }
                return result;
             }
}
