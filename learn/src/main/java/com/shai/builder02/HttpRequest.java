package com.shai.builder02;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder 进阶示例：HttpRequest 构建器
 *
 * <p>演示 Builder 中不只是简单 set，还包含：
 * <ul>
 *   <li>参数校验（URL 格式、超时范围）</li>
 *   <li>方法调用（添加 Header、添加 QueryParam）</li>
 *   <li>条件逻辑（自动追加 Content-Type、自动生成 RequestId）</li>
 *   <li>格式转换（超时单位换算、URL 拼接 QueryString）</li>
 *   <li>build() 中的最终校验与组装</li>
 * </ul>
 */
public class HttpRequest {

    private final String method;
    private final String url;
    private final List<String> headers;
    private final String body;
    private final int timeoutMs;
    private final String requestId;
    private final LocalDateTime createdAt;

    private HttpRequest(Builder builder) {
        this.method = builder.method;
        this.url = builder.buildUrl();
        this.headers = Collections.unmodifiableList(builder.headers);
        this.body = builder.body;
        this.timeoutMs = builder.timeoutMs;
        this.requestId = builder.requestId;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "\n=== HttpRequest ===" +
                "\n  requestId : " + requestId +
                "\n  createdAt : " + createdAt.format(fmt) +
                "\n  method    : " + method +
                "\n  url       : " + url +
                "\n  headers   : " + headers +
                "\n  body      : " + body +
                "\n  timeoutMs : " + timeoutMs + " ms";
    }

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static Builder newRequest(String method, String baseUrl) {
        return new Builder(method, baseUrl);
    }

    public static final class Builder {

        private final String method;
        private final String baseUrl;
        private final List<String> queryParams = new ArrayList<>();
        private final List<String> headers = new ArrayList<>();
        private String body;
        private int timeoutMs = 5_000;
        private String requestId;
        private LocalDateTime createdAt;

        private Builder(String method, String baseUrl) {
            if (method == null || method.isBlank()) {
                throw new IllegalArgumentException("HTTP method 不能为空");
            }
            if (baseUrl == null || !baseUrl.startsWith("http")) {
                throw new IllegalArgumentException("baseUrl 必须以 http/https 开头，当前值：" + baseUrl);
            }
            this.method = method.toUpperCase();
            this.baseUrl = baseUrl;
        }

        /**
         * 添加请求头（key: value 格式）
         * Builder 内部做格式化，调用方只需传 key/value
         */
        public Builder header(String key, String value) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Header key 不能为空");
            }
            // 方法调用：格式化后加入列表
            headers.add(key.trim() + ": " + (value == null ? "" : value.trim()));
            return this;
        }

        /**
         * 添加 Query 参数（自动 URL 编码拼接）
         */
        public Builder queryParam(String key, Object value) {
            if (key != null && !key.isBlank()) {
                // 方法调用：转字符串并追加
                queryParams.add(key + "=" + value);
            }
            return this;
        }

        /**
         * 设置 JSON Body，同时自动追加 Content-Type 头
         * 条件逻辑：有 body 才加 Content-Type
         */
        public Builder jsonBody(String json) {
            this.body = json;
            // 条件逻辑：自动追加 Content-Type
            header("Content-Type", "application/json;charset=UTF-8");
            return this;
        }

        /**
         * 设置超时（秒），Builder 内部换算为毫秒
         * 格式转换：秒 → 毫秒
         */
        public Builder timeoutSeconds(int seconds) {
            if (seconds <= 0 || seconds > 300) {
                throw new IllegalArgumentException("超时时间必须在 1~300 秒之间，当前值：" + seconds);
            }
            // 单位换算
            this.timeoutMs = seconds * 1_000;
            return this;
        }

        /**
         * 手动指定 RequestId；若不调用，build() 时自动生成
         */
        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        /**
         * 组装最终 URL（baseUrl + QueryString）
         */
        private String buildUrl() {
            if (queryParams.isEmpty()) {
                return baseUrl;
            }
            return baseUrl + "?" + String.join("&", queryParams);
        }

        /**
         * 最终构建：自动生成 requestId、createdAt，并做整体校验
         */
        public HttpRequest build() {
            // 自动生成 requestId（若未手动指定）
            if (requestId == null || requestId.isBlank()) {
                requestId = "REQ-" + System.currentTimeMillis();
            }
            // 自动记录创建时间
            createdAt = LocalDateTime.now();

            // 整体校验：POST/PUT 必须有 body
            if (("POST".equals(method) || "PUT".equals(method)) && (body == null || body.isBlank())) {
                throw new IllegalStateException(method + " 请求必须提供 body");
            }

            return new HttpRequest(this);
        }
    }

    // -----------------------------------------------------------------------
    // 演示入口
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        // 示例 1：GET 请求，带 QueryParam 和自定义 Header
        HttpRequest getRequest = HttpRequest
                .newRequest("GET", "https://api.example.com/users")
                .queryParam("page", 1)
                .queryParam("size", 20)
                .queryParam("keyword", "shai")
                .header("Authorization", "Bearer token-abc123")
                .header("Accept", "application/json")
                .timeoutSeconds(10)
                .build();

        System.out.println(getRequest);

        // 示例 2：POST 请求，带 JSON Body（自动追加 Content-Type）
        HttpRequest postRequest = HttpRequest
                .newRequest("POST", "https://api.example.com/users")
                .header("Authorization", "Bearer token-abc123")
                .jsonBody("{\"username\":\"shai\",\"email\":\"shai@example.com\"}")
                .timeoutSeconds(30)
                .requestId("MY-REQ-001")
                .build();

        System.out.println(postRequest);
    }
}
