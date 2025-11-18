package com.shai.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * RabbitMQ 工具类
 */
public class RabbitUtils {
    /**
     * 获取连接通道
     *
     * @return
     * @throws Exception
     */
    public static Channel getChannel() throws Exception {
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

        return channel;
    }
}
