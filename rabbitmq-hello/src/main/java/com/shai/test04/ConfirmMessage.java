package com.shai.test04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;
import com.shai.utils.RabbitUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description: 发布确认
 * @Author: shai
 * @CreateTime: 2025-11-16  20:25
 * @Version: 1.0
 */
public class ConfirmMessage {
    // 批量发布消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 单个确认 发布1000个单独确认消息,耗时：127ms
        // ConfirmMessage.publishMessageIndividually();
        // 批量发布确认
        // ConfirmMessage.publishMessageBatch();
        // 异步发布确认
        ConfirmMessage.publishMessageAsync();
    }

    /**
     * @description: 单个发布确认
     * @author: shai
     * @date: 2025/11/16 20:27
     * @return: 发布1000个单独确认消息, 耗时：127ms
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
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", queueName, null, (message + i).getBytes());
            // 等待消息发送完毕
            if (channel.waitForConfirms()) {
                System.out.println("消息发送成功" + i);
            } else {
                System.out.println("消息发送失败" + i);
                return;
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时：" + (end - begin) + "ms");
    }

    /**
     * @description: 批量发布确认
     * @author: shai
     * @date: 2025/11/16 20:27
     * @return: 批量发布1000个单独确认消息, 耗时：30ms
     **/
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 批量确认消息大小
        int batchSize = 100;
        long begin = System.currentTimeMillis();
        String message = "hello world";
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", queueName, null, (message + i).getBytes());
            // 判断达到批量消息大小
            if (i % batchSize == 0) {
                // 批量确认
                if (channel.waitForConfirms()) {
                    System.out.println("消息发送成功" + i);
                } else {
                    System.out.println("消息发送失败" + i);
                    return;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量发布" + MESSAGE_COUNT + "个单独确认消息,耗时：" + (end - begin) + "ms");
    }

    /**
     * @description: 异步发布确认
     * @author: shai
     * @date: 2025/11/16 20:27
     * @return: 批量发布1000个单独确认消息, 耗时：35ms
     **/
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        String message = "hello world";
        // 创建一个线程安全有序的哈希表 适用于高并发
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        // 添加一个异步监听器(在发送消息前) 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 批量确认
            if (multiple) {
                // 删除已确认的消息,剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else { // 单个确认
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("已确认的消息：" + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 打印未确认的消息
            String msg = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息：" + msg + ";;;tag:" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", queueName, null, (message + i).getBytes());
            // 添加消息到待确认列表
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("异步发布" + MESSAGE_COUNT + "个单独确认消息,耗时：" + (end - begin) + "ms");
    }
}
