package com.https.rabbitmq.delay;


import lombok.Getter;

/**
 * @Auther: zouren
 * @Date: 2019/5/20 16:23
 * @Description:
 */
@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    MESSAGE_QUEUE("order-test", "order-test", "order-test"),
    /**
     * 插件 消息通知ttl队列
     */
    MESSAGE_TTL_QUEUE("order-test.ttl", "order-test.ttl", "order-test.ttl"),
    /**
     * 消息通知ttl队列
     */
    MESSAGE_TTL_QUEUE2("order-test.ttl2", "order-test.ttl2", "order-test.ttl2");
    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
