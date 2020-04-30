package com.https.rabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置序列化方式（默认为application/x-java-serialized-object）
 * 不使用时注释configuration
 */
@Configuration
public class MyConfig {
    @Bean
    public MessageConverter messageConverter(){
        //序列化成json发送
        return new Jackson2JsonMessageConverter();
    }
}
