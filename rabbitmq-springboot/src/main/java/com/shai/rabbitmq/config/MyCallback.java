package com.shai.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 回调实现类
 * 1. 实例化MyCallback
 * 2. @Autowired注入
 * 3. @PostConstruct注入
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    // ⬇️ 此时还不能调用其MyCallback的confirm方法，需要注入到RabbitTemplate

    /**
     * @param correlationData 保存回调消息的ID及相关信息
     * @param ack             交换机是否收到消息
     * @param cause           失败原因
     * @desc 交换机确认回调方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) { // 成功
            log.info("MyCallback：交换机收到消息，ID: {}", id);
        } else { // 失败
            log.info("MyCallback：交换机未收到消息，ID:{}, CAUSE: {}", id, cause);
        }
    }

    /**
     * @desc 队列确认回调方法
     */
    public void returnedMessage() {
        returnedMessage(null);
    }

    /**
     * @param returnedMessage 退回的消息
     * @desc 队列确认回调方法
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("MyCallback：消息 {} 被交换机 {} 退回，退回原因：{}，路由Key：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());

    }
}