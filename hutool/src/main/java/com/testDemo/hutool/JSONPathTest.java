package com.testDemo.hutool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.junit.jupiter.api.Test;

/**
 * @author : zouren
 * @date : 2020/3/26 14:15
 */
public class JSONPathTest {
    @Test
    public void testSetV(){
        JSONObject root = new JSONObject();
        JSONPath.set(root,"$.clientRightItem.ocId","权益责任ID");
        JSONPath.set(root,"$.applier.name","name");
        JSONPath.set(root,"$.applier.certType","certType");
        JSONPath.set(root,"$.applier.certNo","certNo");
        JSONPath.set(root,"$.applier.phoneNo","phoneNo");
        JSONPath.set(root,"$.applier.name","name1");
        JSONPath.set(root,"$.applier.certType","certType1");
        JSONPath.set(root,"$.applier.certNo","certNo1");
        JSONPath.set(root,"$.applier.phoneNo","phoneNo1");

        System.out.println(root);
    }
    @Test
    public void testSetV1(){
        JSONArray root = new JSONArray();
        JSONObject row = new JSONObject();
        row.put("a","a1");
        root.add(row);
        JSONObject row1 = new JSONObject();
        row1.put("a","a2");
        root.add(row1);
        Object  o= JSONPath.eval(root,"$");
        System.out.println(o);
//        JSONPath.set(root,"$.clientRightItem.ocId","权益责任ID");

    }
}
