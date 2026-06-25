package com.shai.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Spring AI 对话控制器
 * ChatClient 已在 AiChatClientConfig 中自动配置（含所有 @Tool 工具）
 * 此处只需直接注入使用
 */
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    /**
     * 直接注入配置好的 ChatClient（已在 AiChatClientConfig 中自动注册所有 @Tool 工具）
     * 无需手动添加工具，新增工具只需加 @Component 和 @Tool 注解即可
     */
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 同步对话 - 简单模式
     * GET /ai/chat?message=你好
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "你好，请介绍一下Spring Boot") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * 流式对话（SSE）- 逐字输出
     * GET /ai/stream?message=介绍一下Spring框架
     * produces = text/event-stream 让浏览器按 SSE 协议解析
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    /**
     * 获取完整的 ChatResponse（包含 token 使用量等元数据）
     * GET /ai/response?message=你好
     */
    @GetMapping("/response")
    public ChatResponse fullResponse(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    /**
     * 带自定义 system prompt 的对话
     * GET /ai/ask?question=什么是微服务&role=架构师
     */
    @GetMapping("/ask")
    public String ask(
            @RequestParam String question,
            @RequestParam(defaultValue = "技术专家") String role) {
        return chatClient.prompt()
                .system("你现在扮演一位" + role + "，请专业地回答以下问题")
                .user(question)
                .call()
                .content();
    }

    /**
     * 多轮对话示例 - 通过参数传递上下文
     * GET /ai/multi?question=那SpringCloud呢
     */
    @GetMapping("/multi")
    public String multiTurn(
            @RequestParam String question,
            @RequestParam(required = false) String context) {
        var prompt = chatClient.prompt();
        if (context != null && !context.isEmpty()) {
            prompt = prompt.system("之前的对话上下文：" + context);
        }
        return prompt.user(question).call().content();
    }
}
