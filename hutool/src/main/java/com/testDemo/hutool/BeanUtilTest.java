package com.testDemo.hutool;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.testDemo.bean.A;
import com.testDemo.bean.A2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zouren
 * @date: 2020/8/18 10:32
 * @description:
 */
public class BeanUtilTest {
    @Test
    public void test1(){
        A a = new A();
        a.setCodename("aa");
        A2 a2 = new A2();
        BeanUtil.copyProperties(a,a2,true);
        System.out.println(a2.getCodeName());
    }
    @Test
    public void test2(){

      JSON list = JSON.parseArray("[{codeNAme:\"a\",id:\"1\"},{codeNAme:\"b\",id:\"2\"}]");
        A a = formatData(list,A.class);
        System.out.println(a);
        List<A> as = formatListData((JSONArray) list,A.class);
        System.out.println();
      JSON object = JSON.parseObject("{codename:\"a\",id:\"1\"}");
        a = formatData(object,A.class);
        System.out.println(a);

    }

    static  <T>T formatData(JSON data, Class<T> tClass){
        T re = null;
        try {
            if(data instanceof JSONObject){
                re = tClass.newInstance();
                BeanUtil.fillBeanWithMapIgnoreCase((JSONObject)data,re,false);
            }else {
                JSONArray dataList  = (JSONArray)data;
                re = dataList.stream().map(row->(JSONObject)row).map(row->{
                    T emp = null;
                    try {
                        emp = tClass.newInstance();
                        BeanUtil.fillBeanWithMapIgnoreCase(row,emp,false);
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                    return emp;
                }).findFirst().get();
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return re;
    }
    static <T> List<T> formatListData(JSONArray data, Class<T> tClass){
        return data.stream().map(row->(JSONObject)row).map(row->{
            T emp = null;
            try {
                emp = tClass.newInstance();
                BeanUtil.fillBeanWithMapIgnoreCase(row,emp,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return emp;
        }).collect(Collectors.toList());
    }
}
