package com.shai.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;

/**
 * 接收者1：监听info、warn的消息
 */
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        channel.queueDeclare("console", false, false, false, null);
        // 绑定
        channel.queueBind("console", EXCHANGE_NAME, "info");
        // 多重绑定
        channel.queueBind("console", EXCHANGE_NAME, "warn");
        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01接收消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console", deliverCallback, consumerTag -> {
        });
    }
}