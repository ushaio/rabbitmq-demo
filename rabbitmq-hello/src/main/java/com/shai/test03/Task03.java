package com.shai.test03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.shai.utils.RabbitUtils;

import java.util.Scanner;

/**
 * @Description: 生产者 手动应答确认
 * @Author: shai
 * @CreateTime: 2025-11-16  14:56
 * @Version: 1.0
 */
public class Task03 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("Task02发送消息完成：" + message);
        }
    }
}
