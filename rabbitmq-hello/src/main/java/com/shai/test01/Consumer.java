package com.shai.test01;

import com.rabbitmq.client.*;

/**
 * 消费者
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 接收消息
    public static void main(String[] args) throws Exception {
        // 创建一个ConnectionFactory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("shai");
        // 创建 Connection
        Connection connection = factory.newConnection();
        // 创建 Channel
        Channel channel = connection.createChannel();
        // 声明 接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收到消息：" + message);
        };
        // 声明 取消消息的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        // 接收消息（队列名称、消费成功后是否自动应答、消费未成功消费的回调、消费者取消消费的回调）
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println("消息接收结束！ ");
    }
}
