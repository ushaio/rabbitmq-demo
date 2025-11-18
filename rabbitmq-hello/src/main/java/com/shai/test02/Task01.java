package com.shai.test02;

import com.rabbitmq.client.Channel;
import com.shai.utils.RabbitUtils;

import java.util.Scanner;

/**
 * @Description: 生产者
 * @Author: shai
 * @CreateTime: 2025-11-16  14:09
 * @Version: 1.0
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    // 发送者
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 发送一个消费（发送到的交换机、路由key-队列的名称、其他参数信息、发送的消息体）
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}
