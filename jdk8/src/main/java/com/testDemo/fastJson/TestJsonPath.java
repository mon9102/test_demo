package com.testDemo.fastJson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.junit.jupiter.api.Test;

/**
 * @author : zouren
 * @date : 2020/3/16 16:53
 */

public class TestJsonPath {


    @Test
    public void test1(){
        Entity entity = new Entity(123, new Object());
        System.out.println();
       System.out.println(entity.getValue()+"   "+ JSONPath.eval(entity, "$.value"));
        System.out.println(JSONPath.contains(entity, "$.value"));
        System.out.println(JSONPath.containsValue(entity, "$.id", 123));
        System.out.println(JSONPath.containsValue(entity, "$.value", entity.getValue()));
        System.out.println(2+"   "+JSONPath.size(entity, "$"));
        System.out.println(0+"   "+ JSONPath.size(new Object(), "$"));
    }
    @Test
    public void test2(){
        String json = "{\n" +
                "  \"status\": 0,\n" +
                "  \"msg\": \"aa\",\n" +
                "  \"data\": {\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"ocId\": \"权益ID \",\n" +
                "        \"orderNo\": \"权益ID \",\n" +
                "        \"ifRightItemFixed\": \"权益责任是否已固定 \",\n" +
                "        \"productInPack\": {\n" +
                "          \"productPack\": \"所属的服务产品\",\n" +
                "          \"servTimes\": \"服务次数\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"ocId\": \"1 \",\n" +
                "        \"orderNo\": \"1 \",\n" +
                "        \"ifRightItemFixed\": \"1\",\n" +
                "        \"productInPack\": {\n" +
                "          \"productPack\": \"1\",\n" +
                "          \"servTimes\": \"1\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}\n";
        JSONObject jsonObject = JSONObject.parseObject(json);
        Boolean bb = (Boolean) JSONPath.eval(jsonObject,"$.data.list1[0].ocId");
        System.out.println(bb);
        JSONArray aa = (JSONArray)JSONPath.eval(jsonObject,"$.data.list");
        System.out.println(aa);
    }
    public static class Entity {
        private Integer id;
        private String name;
        private Object value;

        public Entity() {}
        public Entity(Integer id, Object value) { this.id = id; this.value = value; }
        public Entity(Integer id, String name) { this.id = id; this.name = name; }
        public Entity(String name) { this.name = name; }

        public Integer getId() { return id; }
        public Object getValue() { return value; }
        public String getName() { return name; }

        public void setId(Integer id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setValue(Object value) { this.value = value; }
    }
}
