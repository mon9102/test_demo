package com.sinosoft.rabbitmq.config.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RabbitSender {

    @Autowired
    private AmqpTemplate rabbitAmqpTemplate;

    //交换器名称
    @Value("${mq.exchange.name1}")
    private String exchange1;

    //交换器名称
    @Value("${mq.exchange.name2}")
    private String exchange2;

    //direct路由键
    @Value("${mq.routingkey.icc.direct.key1}")
    private String routingkey1;

    //topic路由键
    @Value("${mq.routingkey.icc.topic.key1}")
    private String routingkey2;

    //队列名
    @Value("${mq.queue.name1}")
    private String queueName1;


    /**
     * 指定队列
     * 调用时传入队列名和String消息
     * @param queueName 队列名
     * @param message  内容
     */
    public void sendString(String queueName,String message){
        rabbitAmqpTemplate.convertAndSend(queueName,message);
    }

    /**
     * 指定队列
     * 调用时传入队列名和map消息
     * @param queueName 队列名
     * @param message 内容
     */
    public void sendMap(String queueName, Map<String,Object> message){
        rabbitAmqpTemplate.convertAndSend(queueName,message);
    }

    /**
     * 发送到默认队列
     * @param message 内容
     */
    public void sendDefaultQueue(Map<String,Object> message){
        rabbitAmqpTemplate.convertAndSend(queueName1,message);
    }


    /**
     * 传入交换机路由键来指定发送队列
     * @param exchange 交换机
     * @param routingkey 路由键
     * @param message 消息
     */
    public void sendExchange(String exchange, String routingkey, Map<String,Object> message){
        rabbitAmqpTemplate.convertAndSend(exchange,routingkey,message);
    }

    /**
     * 采用默认Direct交换机和路由键
     * @param message 消息
     */
    public void sendDefaultDirectExchange(Map<String,Object> message){
        rabbitAmqpTemplate.convertAndSend(exchange1,routingkey1,message);
    }

    /**
     * 采用默认topic交换机和路由键
     * @param message 消息
     */
    public void sendDefaultTopicExchange(Map<String,Object> message){
        rabbitAmqpTemplate.convertAndSend(exchange2,routingkey2,message);
    }
}
