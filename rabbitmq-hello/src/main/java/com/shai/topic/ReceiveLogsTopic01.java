package com.shai.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;

/**
 * 接收者1
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        // 绑定 匹配规则：*.orange.*
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("ReceiveLogsTopic01等待接收消息...");
        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01接收消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收队列：" + queueName + ";;;绑定键：" + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName, deliverCallback, consumerTag -> {
        });
    }
}
