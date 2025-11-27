package com.shai.rabbitmq.controller;

import com.shai.rabbitmq.config.ConfirmConfig;
import com.shai.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description: 发送消息
 * @Author: shai
 * @CreateTime: 2025-11-23  15:07
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    @RequestMapping("sendMsg/{message}")
    public String sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送消息给两个TTL队列：{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的QA队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的QB队列：" + message);
        return "发送消息成功";
    }

    /**
     * @description: 基于上面的方法，无法设置自定义的ttl时间
     * @author: shai
     * @date: 2025/11/23 16:15
     * @param: [ttl, message]
     * @return: java.lang.String
     **/
    @RequestMapping("sendExpirationMsg/{ttl}/{message}")
    public String sendExpirationMsg(@PathVariable String ttl, @PathVariable String message) {
        log.info("当前时间：{}，发送一条时长 {}ms 的消息给QC TTL队列：{}", new Date(), ttl, message);
        // 通过XC实现交换机X给QC队列发送消息
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
        return "发送消息成功";
    }

    /**
     * 延迟队列
     *
     * @param message
     * @param delayTime
     * @return
     */
    @RequestMapping("/sendDelayedMsg/{message}/{delayTime}")
    public String sendDelayedMsg(@PathVariable String message, @PathVariable Long delayTime) {
        log.info("当前时间：{}，发送一条时长 {}ms 的消息给延迟队列：{}", new Date(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
                    // 发送消息 延迟时间（ms）
                    msg.getMessageProperties().setDelayLong(delayTime);
                    return msg;
                });
        return "DelayedMsg消息发送成功！" + message;
    }

    @RequestMapping("/sendConfirmMsg/{message}/{delayTime}")
    public String sendConfirmMsg(@PathVariable String message, @PathVariable Long delayTime) {
        log.info("当前时间：{}，发送一条时长 {}ms 的消息给延迟队列：{}", new Date(), delayTime, message);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message, msg -> {
                    // 发送消息 延迟时间（ms）
                    msg.getMessageProperties().setDelayLong(delayTime);
                    return msg;
                });
        return "DelayedMsg消息发送成功！" + message;
    }
}
