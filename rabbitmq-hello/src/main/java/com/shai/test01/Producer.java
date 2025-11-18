package com.shai.test01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发消息
 */
public class Producer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个ConnectionFactory
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 rabbitmq 的 ip 端口 默认端口是 5672
        factory.setHost("127.0.0.1");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("shai");
        // 创建 Connection
        Connection connection = factory.newConnection();
        // 创建 Channel
        Channel channel = connection.createChannel();
        // 创建队列（队列名称、是否持久化、是否只供一个消费者消费、是否自动删除）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发送消息
        String message = "I love u";
        // 发送一个消费（发送到的交换机、路由key-队列的名称、其他参数信息、发送的消息体）
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕！");

    }
}
