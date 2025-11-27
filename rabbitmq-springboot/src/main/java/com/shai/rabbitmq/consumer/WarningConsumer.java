package com.shai.rabbitmq.consumer;

import com.shai.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警队列 消费者
 */
@Slf4j
@Component
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void reveiveD(Message message) throws Exception {
        String msg = new String(message.getBody());
        log.info("WarningConsumer：报警发现不可路由消息：{}", msg);
    }
}
