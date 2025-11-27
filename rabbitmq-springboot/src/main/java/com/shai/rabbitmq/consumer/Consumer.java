package com.shai.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.shai.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void reveiveD(Message message) throws Exception {
        String msg = new String(message.getBody());
        log.info("Consumer：收到 {} 队列的消息：{}", ConfirmConfig.CONFIRM_QUEUE_NAME, msg);
    }
}
