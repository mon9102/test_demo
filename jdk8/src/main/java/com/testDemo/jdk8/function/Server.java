package com.testDemo.jdk8.function;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zouren
 * @Date: 2019/3/26 13:32
 * @Description:
 */
public class Server implements IServer{
    @Override
   public Map<String,String> aa(String a){
       Map re = new HashMap(10);
       re.put("a",a);
       return re;
   }
    @Override
    public Map<String,String> cc(String a){
        Map re = new HashMap(10);
        re.put("c",a);
        return re;
    }
}
