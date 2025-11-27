package com.shai.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.shai.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者 基于插件消息延迟
 */
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void reveiveD(Message message) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列 {} 的消息：{}", DelayedQueueConfig.DELAYED_QUEUE_NAME, new Date(), msg);
    }
}
