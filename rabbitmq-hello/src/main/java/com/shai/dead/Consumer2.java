package com.shai.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者2
 */
public class Consumer2 {
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("Consumer2等待接收消息...");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer2接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(DEAD_QUEUE, deliverCallback, consumerTag -> {
        });
    }
}
