package com.shai.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.shai.utils.RabbitUtils;

/**
 * 生产者
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 延迟（死信）消息，设置TTL
        // 如果10s内消费者C1未被接受，则转到死信队列给C2
        // AMQP.BasicProperties properties = new AMQP.BasicProperties()
        //         .builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String message = "info-" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
        }
        channel.queueDeclare();
    }
}
