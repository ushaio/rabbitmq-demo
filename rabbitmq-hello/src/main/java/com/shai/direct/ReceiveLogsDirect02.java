package com.shai.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;

/**
 * 接收者2：监听error级别的消息
 */
public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        channel.queueDeclare("disk", false, false, false, null);
        // 绑定
        channel.queueBind("disk", EXCHANGE_NAME, "error");
        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02接收消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("disk", deliverCallback, consumerTag -> {
        });
    }
}