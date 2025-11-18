package com.shai.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.shai.utils.RabbitUtils;

import java.util.Scanner;

/**
 * @Description: 发送消息
 * @Author: shai
 * @CreateTime: 2025-11-16 22:46:00
 * @Version: 1.0
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("EmitLog发送消息完成：" + message);
        }
    }
}
