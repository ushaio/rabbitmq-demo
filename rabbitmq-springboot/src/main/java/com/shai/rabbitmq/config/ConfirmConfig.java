package com.shai.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布确认（高级）
 */
@Configuration
public class ConfirmConfig {
    // 交换机
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // 队列
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // routingkey
    public static final String CONFIRM_ROUTING_KEY = "confirm.routingkey";

    /**
     * 声明队列
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    /**
     * 声明交换机
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    /**
     * 延迟队列 绑定 延迟交换机
     */
    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmQueue") Queue confirmQueue,
            @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }
}
