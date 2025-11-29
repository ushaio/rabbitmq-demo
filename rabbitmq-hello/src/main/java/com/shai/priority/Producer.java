package com.shai.priority;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.shai.utils.RabbitUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 生产者 优先级队列
 */
public class Producer {
    private static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);

        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);
        // i = 5，设置优先级 ：
        for (int i = 0; i < 10; i++) {
            String message = "消息" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
    }

}
