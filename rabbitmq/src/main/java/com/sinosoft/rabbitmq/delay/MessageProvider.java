package com.sinosoft.rabbitmq.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Auther: zouren
 * @Date: 2019/5/20 16:26
 * @Description:
 */
@Component
@Slf4j
public class MessageProvider {

    /**
     * RabbitMQ 模版消息实现类
     */
    @Autowired
    private AmqpTemplate rabbitMqTemplate;

    /**
     * 发送延迟消息
     *
     * @param messageContent 消息内容
     * @param exchange       队列交换
     * @param routerKey      队列交换绑定的路由键
     * @param delayTimes     延迟时长，单位：毫秒
     */
    public void sendMessage(Object messageContent, String exchange, String routerKey, final long delayTimes) {
        if (!StringUtils.isEmpty(exchange)) {
            log.info("延迟：{}毫秒写入消息队列：{}，消息内容：{}", delayTimes, routerKey, messageContent);
            // 执行发送消息到指定队列
            rabbitMqTemplate.convertAndSend(exchange, routerKey, messageContent, message -> {
                // 设置延迟毫秒值
                message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                return message;
            });
        } else {
            log.error("未找到队列消息：{}，所属的交换机", exchange);
        }
    }

    /**
     * 插件
     * @param msg
     * @param delayTimes
     */
    public void sendMsg(String msg, final long delayTimes) {
        log.info(msg);
        rabbitMqTemplate.convertAndSend(QueueEnum.MESSAGE_TTL_QUEUE.getExchange(), QueueEnum.MESSAGE_TTL_QUEUE.getRouteKey(), msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay", delayTimes);
                return message;
            }
        });
    }
}
