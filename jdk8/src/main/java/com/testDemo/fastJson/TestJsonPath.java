package com.testDemo.fastJson;

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
