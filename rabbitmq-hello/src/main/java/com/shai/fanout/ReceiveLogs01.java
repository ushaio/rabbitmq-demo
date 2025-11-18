package com.shai.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;

/**
 * @Description: 消息接收
 * @Author: shai
 * @CreateTime: 2025-11-16  22:30
 * @Version: 1.0
 */
public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明临时队列(断开连接即删除)
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列到交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "tstkey");
        System.out.println("ReceiveLogs01等待接收消息...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("控制台ReceiveLogs01打印接收到消息：" + message);
        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback -> {
        });
    }
}
