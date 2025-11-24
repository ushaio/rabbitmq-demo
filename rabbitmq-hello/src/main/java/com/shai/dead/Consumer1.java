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
//        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定普通交换机与普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 绑定死信交换机与死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("Consumer1等待接收消息...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (msg.equals("info-5")) {
                // 拒绝消息处理，移交死信（C1消费9条，C2消费1条）
                System.out.println("Consumer1拒绝了消息❌：" + msg);
                // false：不放回队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer1接收到消息：" + msg);
                // false：不批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
