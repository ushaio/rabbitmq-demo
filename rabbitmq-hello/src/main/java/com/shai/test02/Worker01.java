package com.shai.test02;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

/**
 * @description:
 * @author: shai
 * @date: 2025/11/16 11:34
 **/
public class Worker01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 消息接收
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收到的消息：" + message);
        };
        // 消息接收被取消
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息接收被中断");
        };

        System.out.println("C2等待接收消息...");
        // 接收消息（队列名称、消费成功后是否自动应答、消费成功消费的回调、消费者取消消费的回调）
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
