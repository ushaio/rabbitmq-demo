package com.shai.rabbitmq.controller;

import com.shai.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        final String defaultMsg = message;
        // 正常
        // CorrelationData correlationData = new CorrelationData("1");
        // message = defaultMsg + correlationData.getId();
        // rabbitTemplate.convertAndSend(
        //         ConfirmConfig.CONFIRM_EXCHANGE_NAME,
        //         ConfirmConfig.CONFIRM_ROUTING_KEY,
        //         message, correlationData);
        // log.info("发送消息内容：{}", message);
        // log.info("----------------------------");
        // 错误的交换机
        // CorrelationData correlationData2 = new CorrelationData("2");
        // message = defaultMsg + correlationData2.getId();
        // rabbitTemplate.convertAndSend(
        //         ConfirmConfig.CONFIRM_EXCHANGE_NAME + "2",
        //         ConfirmConfig.CONFIRM_ROUTING_KEY,
        //         message, correlationData2);
        // log.info("发送消息内容：{}", message);
        // log.info("----------------------------");
        // 错误的routingkey
        CorrelationData correlationData3 = new CorrelationData("3");
        message = defaultMsg + correlationData3.getId();
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "3",
                message, correlationData3);
        log.info("发送消息内容：{}", message);
        log.info("----------------------------");

        return "发送消息成功：" + message;
    }
}
