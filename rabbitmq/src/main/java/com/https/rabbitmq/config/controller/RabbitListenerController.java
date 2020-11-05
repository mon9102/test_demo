package com.https.rabbitmq.config.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 不想监听时注掉@RestController即可
 */
@RestController
public class RabbitListenerController {
    //用注解监听队列icc
    //使用object时收到：icc收到消息：(Body:'{"aaa":"aaa","bbb":["ccc",222]}' MessageProperties [headers={__ContentTypeId__=java.lang.Object, __KeyTypeId__=java.lang.Object, __TypeId__=java.util.HashMap}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=icc.direct, receivedRoutingKey=icc, deliveryTag=2, consumerTag=amq.ctag-VztdQ5OOykJfIJweb1ipTQ, consumerQueue=icc])
//    @RabbitListener(queues = "icc")
    public void listenerIcc(Object o){
        System.out.println("icc收到消息："+o);
    }
    //用注解监听队列icc.test1
    //使用map时收到：icc.test1收到消息：{aaa=aaa, bbb=[ccc, 222]}
//    @RabbitListener(queues = "icc.test1")
    public void listener(HashMap<String,Object> map){
        System.out.println("icc.test1收到消息："+map);
    }

    /**
     *
     * @param map 传参map
     * @RabbitListener bindings：绑定队列
     * @QueueBinding value：绑定队列名
     *              key：路由键
     * @Exchange value：交换机名称
     *              type：交换机类型
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "${mq.queue.name3}"),
//            exchange = @Exchange(value = "${mq.exchange.name1}",type = ExchangeTypes.DIRECT),
//            key = "${mq.routingkey.icc.direct.key3}"
//    ))
    public void listenTest(HashMap<String,Object> map){
        System.out.println("icc.test2监听到消息："+map);
    }
}
