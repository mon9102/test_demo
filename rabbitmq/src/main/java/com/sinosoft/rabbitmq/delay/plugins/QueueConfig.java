package com.sinosoft.rabbitmq.delay.plugins;

import com.sinosoft.rabbitmq.delay.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**插件
 * 这里要特别注意的是，使用的是CustomExchange,不是DirectExchange，另外CustomExchange的类型必须是x-delayed-message。
 */
//@Configuration
@Slf4j
public class QueueConfig {

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        log.info("delayExchange");
        return new CustomExchange(QueueEnum.MESSAGE_TTL_QUEUE.getExchange(), "x-delayed-message",true, false,args);
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue(QueueEnum.MESSAGE_TTL_QUEUE.getName(), true);
        log.info("queue");
        return queue;
    }

    @Bean
    public Binding binding() {
        log.info("binding");
        return BindingBuilder.bind(queue()).to(delayExchange()).with(QueueEnum.MESSAGE_TTL_QUEUE.getRouteKey()).noargs();
    }
}

