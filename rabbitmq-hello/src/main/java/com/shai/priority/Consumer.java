package com.shai.priority;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;

/**
 * 消费者
 */
public class Consumer {
    public static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("PriorityQueue等待接收消息...");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("PriorityQueue接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(QUEUE_NAME, deliverCallback, consumerTag -> {
        });
    }
}
