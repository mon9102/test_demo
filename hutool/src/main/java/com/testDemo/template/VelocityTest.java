package com.testDemo.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: zouren
 * @date: 2021/3/12
 * @description:
 */
public class VelocityTest {
    /**
     * 原生的写法 hutools有乱码
     * @param args
     */
    public static void main(String[] args) {

        Properties pro = new Properties();
        pro.setProperty("resource.default_encoding","utf-8");
        pro.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");


        Velocity.init(pro);

        StringWriter sw = new StringWriter();
        Template template =  Velocity.getTemplate("templates/velocity_test.vm","utf-8");
        Map map = new HashMap();
        map.put("name", "Hutool");
        VelocityContext context = new VelocityContext(map);

        template.merge(context, sw);
        System.out.println(sw.toString());
    }
}
