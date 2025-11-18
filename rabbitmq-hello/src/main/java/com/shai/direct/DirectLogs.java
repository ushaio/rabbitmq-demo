package com.shai.direct;

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
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error1", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("DirectLogs：" + message);
        }
    }
}
