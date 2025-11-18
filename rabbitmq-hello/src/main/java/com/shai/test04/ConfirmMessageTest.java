package com.shai.test04;

import com.rabbitmq.client.Channel;
import com.shai.utils.RabbitUtils;

import java.util.UUID;

/**
 * @Description: 发布确认
 * @Author: shai
 * @CreateTime: 2025-11-16  20:25
 * @Version: 1.0
 */
public class ConfirmMessageTest {
    // 批量发布消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 单个确认
        ConfirmMessageTest.publishMessageIndividually(); // 101ms
    }

    /**
     * @description: 单个发布确认
     * @author: shai
     * @date: 2025/11/16 20:27
     **/
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();
        String message = "hello world";
        channel.basicPublish("", queueName, null, (message).getBytes());
        // 等待消息发送完毕
        if (channel.waitForConfirms()) {
            System.out.println("消息发送成功");
        } else {
            System.out.println("消息发送失败");
            return;
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时：" + (end - begin) + "ms");
    }
}
