package com.shai.test03;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.shai.utils.RabbitUtils;
import com.shai.utils.SleepUtils;

/**
 * @Description: 消费者
 * @Author: shai
 * @CreateTime: 2025-11-16  15:07
 * @Version: 1.0
 */
public class Worker031 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        int flag = 10;
        int prefetchCount = 7;
        Channel channel = RabbitUtils.getChannel();
        System.out.println("C" + flag + "开始接受消息(" + flag + "s)...");
        System.out.println("预取值：" + prefetchCount);
        // 接收消息成功应答
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            SleepUtils.sleep(flag);
            System.out.println("接收到的消息：" + new String(delivery.getBody(), "UTF-8"));
            // 消息完成后 手动应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        // 设置为不公平分发/预取值
        channel.basicQos(prefetchCount);
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
