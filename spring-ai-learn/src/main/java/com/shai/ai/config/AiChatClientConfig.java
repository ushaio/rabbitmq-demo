package com.shai.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * ChatClient 自动配置
 * 自动扫描 Spring 容器中包含 @Tool 方法的 Bean，并注册到 ChatClient
 * 
 * 新增工具只需在类上加 @Component，方法上加 @Tool，无需修改此配置或 ChatController
 */
@Configuration
public class AiChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ApplicationContext context) {
        // 自动发现所有包含 @Tool 方法的 Spring Bean
        Object[] tools = context.getBeansOfType(Object.class).values().stream()
                .filter(bean -> Arrays.stream(bean.getClass().getMethods())
                        .anyMatch(method -> method.isAnnotationPresent(Tool.class)))
                .toArray();

        return builder
                .defaultSystem("你是一个Java/Spring技术专家，请用中文回答问题。你可以查询当前日期时间和天气信息。")
                .defaultTools(tools)
                .build();
    }
}
