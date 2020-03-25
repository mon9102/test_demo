package com.testDemo.hutool;

import cn.hutool.core.bean.DynaBean;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.testDemo.hutool.com.testDemo.bean.CustomerRightsPackageDto;
import com.testDemo.hutool.com.testDemo.bean.CustomerRightsServerDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zouren
 * @date : 2020/3/23 10:14
 */
public class DynaBeanTest {
    @Test
    public void test(){
        JSONArray array_a = new JSONArray();
        JSONObject array_aItem = new JSONObject();
        array_aItem.put("b", "c");
        array_a.add(array_aItem);
        JSONObject array_aItem2 = new JSONObject();
        array_aItem2.put("b", "d");
        array_a.add(array_aItem2);
        DynaBean parant = DynaBean.create(array_a);
        Object o = parant.getBean();
        if(o instanceof List){
            List a = (List)o;
            System.out.println(a);
        }
        System.out.println( "" );

    }
    @Test
    public void jsonPath(){
        JSONObject object = new JSONObject();
        JSONArray array_a = new JSONArray();
        JSONObject array_aItem = new JSONObject();
        array_aItem.put("b", "c");
        array_aItem.put("a", "a");
        array_a.add(array_aItem);
        JSONObject array_aItem2 = new JSONObject();
        array_aItem2.put("b", "d");
        array_aItem2.put("a", "aa");
        array_a.add(array_aItem2);

        JSONObject array_aItem3 = new JSONObject();
        array_aItem3.put("a", "a");
        array_a.add(array_aItem3);
        object.put("array",array_a);
        String proName = "$.array.b";
        Object o2 = JSONPath.eval(object,proName);
        System.out.println(o2);
        System.out.println(proName.lastIndexOf("."));
        String paraProName = proName.substring(0,proName.lastIndexOf("."));
        Object array = JSONPath.eval(object,paraProName);
        System.out.println(array);

    }
    @Test
    public void testObject(){
        CustomerRightsPackageDto customerRightsPackageDto =new CustomerRightsPackageDto();
        customerRightsPackageDto.setName("aa");
        customerRightsPackageDto.setPackageType(1);
        List<CustomerRightsServerDto> customerRightsServerDtoList = new ArrayList<CustomerRightsServerDto>();
        CustomerRightsServerDto row1 = new CustomerRightsServerDto();
        row1.setName("aa.1");
        customerRightsServerDtoList.add(row1);

        CustomerRightsServerDto row2 = new CustomerRightsServerDto();
        row2.setName("aa.2");
        customerRightsServerDtoList.add(row2);

        customerRightsPackageDto.setServerDtoList(customerRightsServerDtoList);
        String proName = "$.serverDtoList.name";
        Object o2 = JSONPath.eval(customerRightsPackageDto,proName);
        System.out.println(o2);
        System.out.println(proName.lastIndexOf("."));
        String paraProName = proName.substring(0,proName.lastIndexOf("."));
        Object array = JSONPath.eval(customerRightsPackageDto,paraProName);
        System.out.println(array);

    }
    @Test
    public void testNull(){
        CustomerRightsPackageDto customerRightsPackageDto =new CustomerRightsPackageDto();

        List<CustomerRightsServerDto> customerRightsServerDtoList = new ArrayList<CustomerRightsServerDto>();
        CustomerRightsServerDto row1 = new CustomerRightsServerDto();
        row1.setName("aa.1");
        customerRightsServerDtoList.add(row1);

        CustomerRightsServerDto row2 = new CustomerRightsServerDto();
        row2.setName("aa.2");
        customerRightsServerDtoList.add(row2);

        customerRightsPackageDto.setServerDtoList(customerRightsServerDtoList);
        String proName = "$.serverDtoList[1]";
        Object o2 = JSONPath.eval(JSONObject.toJSON(customerRightsPackageDto),proName);
        JSONObject jsonObject = (JSONObject) o2;
        System.out.println(o2);

    }
    @Test
    public void testJOSN(){
        JSONObject re = new JSONObject();
        re.put("a","1");
        System.out.println(        re.getInteger("a")
        );
    }



}
