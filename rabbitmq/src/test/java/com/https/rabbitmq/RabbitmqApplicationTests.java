package com.https.rabbitmq;

import com.https.rabbitmq.config.controller.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitSender rabbitSender;

    //rabbitmq发送消息
    @Test
    public void contextLoads() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("aaa","aaa");
        map.put("bbb", Arrays.asList("ccc",222));
        //发送到的交换机
        String exchange = "icc.direct";
        //路由建
        String routingKey = "icc";
        rabbitTemplate.convertAndSend(exchange,routingKey,map);
    }
    //rabbitmq接收消息
    @Test
    public void contextLoads1() {
        //队列名
        String queueName = "icc";
        Object o = rabbitTemplate.receiveAndConvert(queueName);
        System.out.println(o);
    }

    @Test
    public void rabbitSenderTest() {
        rabbitSender.sendString("icc","rabbit sender test");
    }
    @Test
    public void rabbitSenderTest2() {
        Map<String,Object> testMap = new HashMap<>();
        testMap.put("aa",123);
        testMap.put("bb","bb");
        rabbitSender.sendMap("icc",testMap);
    }

    @Test
    public void rabbitSenderTest3() {
        Map<String,Object> testMap = new HashMap<>();
        testMap.put("aa",123);
        testMap.put("bb","bb");
//        rabbitSender.sendDefaultDirectExchange(testMap);
        rabbitSender.sendMap("icc.test1",testMap);
//        rabbitSender.sendDefaultTopicExchange(testMap);
    }
}
