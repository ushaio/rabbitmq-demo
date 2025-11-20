package com.shai.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者1
 */
public class Consumer1 {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明死信和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信和普通队列
        Map<String, Object> arguments = new HashMap<>();
        // 绑定死信队列
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置正常队列长度限制
        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定普通交换机与普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 绑定死信交换机与死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("Consumer1等待接收消息...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer1接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(NORMAL_QUEUE, deliverCallback, consumerTag -> {
        });
    }
}
