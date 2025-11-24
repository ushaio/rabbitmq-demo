package com.shai.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: ttl消费者
 * @Author: shai
 * @CreateTime: 2025-11-23  15:14
 * @Version: 1.0
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    // 接收来自队列 QD 的消息
    @RabbitListener(queues = "QD")
    public void reveiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列QD的消息：{}", new Date(), msg);
    }
}
