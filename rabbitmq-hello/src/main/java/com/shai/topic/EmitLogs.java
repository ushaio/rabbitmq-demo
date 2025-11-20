package com.shai.topic;

import com.rabbitmq.client.Channel;
import com.shai.utils.RabbitUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 发送消息
 */
public class EmitLogs {
    public static final String EXCHANGE_NAME = "topic_logs";

    /**
     * Q1：*.orange.*
     * Q2：*.*.rabbit
     * Q2：lazy.#
     */
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orage.rabbit", "Q1;Q2");
        bindingKeyMap.put("lazy.orange.elephant", "Q1;Q2");
        bindingKeyMap.put("quick.orange.fox", "Q1");
        bindingKeyMap.put("lazy.brown.fox", "Q2");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列Q2接收");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配Q2");
        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String routingKey = bindingKeyEntry.getKey();
            System.out.println("routing-key：" + routingKey);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, bindingKeyEntry.getValue().getBytes());
        }
    }
}
