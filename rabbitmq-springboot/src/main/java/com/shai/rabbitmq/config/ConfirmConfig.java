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
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    // 队列
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    // routingkey
    public static final String CONFIRM_ROUTING_KEY = "key1";
    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

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
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
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

    /**
     * 备份交换机
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    /**
     * 备份队列
     */
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    /**
     * 报警队列
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    /**
     * 绑定备份队列
     */
    @Bean
    public Binding backupQueueBindingExchange(@Qualifier("backupQueue") Queue backupQueue,
                                              @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue)
                .to(backupExchange);
    }
}
