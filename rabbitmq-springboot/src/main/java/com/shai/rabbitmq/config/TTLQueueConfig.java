package com.shai.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TTL延迟队列 配置文件 绑定关系
 * @Author: shai
 * @CreateTime: 2025-11-23  14:43
 * @Version: 1.0
 */
@Configuration
public class TTLQueueConfig {

    // 交换机
    private static final String X_EXCHANGE = "X";
    private static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 普通队列 有TTL
    private static final String QUEUE_A = "QA";
    private static final String QUEUE_B = "QB";
    // 普通队列 无过期时间
    private static final String QUEUE_C = "QC";
    // 死信队列
    private static final String DEAD_LETTER_QUEUE = "QD";

    // 声明X交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // 队列 TTL:10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A)
                .withArguments(arguments)
                .build();
    }

    // 队列 TTL:40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B)
                .withArguments(arguments)
                .build();
    }

    // 队列 （延迟队列优化）
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C)
                .withArguments(arguments)
                .build();
    }

    // 死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    // 绑定 queueA 到 X交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xEchange) {
        return BindingBuilder
                .bind(queueA)
                .to(xEchange)
                .with("XA");
    }

    // 绑定 queueB 到 X交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xEchange) {
        return BindingBuilder
                .bind(queueB)
                .to(xEchange)
                .with("XB");
    }

    // 绑定 X交换机 - QC队列 （延迟优化）
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xEchange) {
        return BindingBuilder
                .bind(queueC)
                .to(xEchange)
                .with("XC");
    }

    // 绑定 queueD 到 Y交换机
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder
                .bind(queueD)
                .to(yExchange)
                .with("YD");
    }

}


