package com.shai.builder02;

/**
 * Builder 模式 vs 普通 setter 写法对比
 *
 * <p>本类通过同一个场景（构建 HttpRequest）展示两种写法的本质差异。
 */
public class BuilderVsSetterComparison {

    // =========================================================================
    // 方式一：传统 setter 写法
    // =========================================================================

    /**
     * 传统 setter 写法的 HttpRequest（可变对象）
     *
     * <p>问题：
     * <ol>
     *   <li><b>对象可变</b>：build 完之后任何人都能继续调用 setter 修改状态，线程不安全</li>
     *   <li><b>无法保证完整性</b>：调用方可能忘记设置必填字段，编译器不报错，运行时才崩</li>
     *   <li><b>校验分散</b>：每个 setter 各自校验，或者根本没有校验，逻辑散落各处</li>
     *   <li><b>顺序无约束</b>：setter 可以任意顺序调用，无法表达"先设置 A 才能设置 B"的依赖关系</li>
     *   <li><b>无法做组合操作</b>：jsonBody() 需要同时设置 body + Content-Type，setter 做不到原子性</li>
     * </ol>
     */
    static class MutableHttpRequest {
        private String method;
        private String url;
        private String body;
        private String contentType;
        private int timeoutMs;

        // setter 只是简单赋值，无法做组合操作
        public void setMethod(String method) { this.method = method; }
        public void setUrl(String url) { this.url = url; }
        public void setBody(String body) { this.body = body; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }

        @Override
        public String toString() {
            return "MutableHttpRequest{method=" + method + ", url=" + url
                    + ", body=" + body + ", contentType=" + contentType
                    + ", timeoutMs=" + timeoutMs + "}";
        }
    }

    // =========================================================================
    // 方式二：Builder 写法（见 HttpRequest.java）
    // =========================================================================
    //
    // Builder 的核心价值不是"链式调用好看"，而是：
    //
    // 1. 【不可变对象】build() 之后字段全是 final，任何人无法修改，天然线程安全
    //
    // 2. 【构造完整性保证】必填参数放在 Builder 构造方法里，编译器强制传入
    //    new Builder("GET", "https://...") ← 不传就编译报错
    //
    // 3. 【集中校验 + 组合操作】
    //    jsonBody(json) 一次调用同时完成：
    //      - 设置 body
    //      - 自动追加 Content-Type 头
    //    setter 写法需要调用方自己记得同时调两个 setter，容易漏
    //
    // 4. 【单位/格式转换封装在 Builder 内部】
    //    timeoutSeconds(30) → 内部换算为 30000ms，调用方不需要关心单位
    //    setter 写法：setTimeoutMs(30 * 1000) ← 调用方自己算，容易出错
    //
    // 5. 【build() 做最终整体校验】
    //    POST 必须有 body，这个规则只在 build() 里写一次，
    //    setter 写法无法在"所有字段都设置完之后"做整体校验
    //
    // 6. 【自动生成派生字段】
    //    build() 自动生成 requestId、createdAt，
    //    setter 写法需要调用方手动设置，或者在某个 init() 方法里，容易忘

    public static void main(String[] args) {
        System.out.println("=== 方式一：setter 写法（问题演示）===");

        MutableHttpRequest req1 = new MutableHttpRequest();
        req1.setMethod("POST");
        req1.setUrl("https://api.example.com/users");
        req1.setBody("{\"username\":\"shai\"}");
        // 问题1：调用方必须自己记得同时设置 Content-Type，容易漏
        req1.setContentType("application/json");
        // 问题2：调用方自己换算单位，容易出错
        req1.setTimeoutMs(30 * 1000);
        // 问题3：build 完之后还能继续修改，破坏对象状态
        req1.setMethod("DELETE"); // 随时可以改，不安全
        System.out.println(req1);

        System.out.println("\n=== 方式二：Builder 写法（HttpRequest）===");

        HttpRequest req2 = HttpRequest
                .newRequest("POST", "https://api.example.com/users")
                // jsonBody 一次调用同时设置 body + Content-Type，原子操作
                .jsonBody("{\"username\":\"shai\"}")
                // 传秒，内部自动换算毫秒
                .timeoutSeconds(30)
                // build() 自动生成 requestId 和 createdAt
                .build();
        // req2 的字段全是 final，build 之后无法修改，安全
        System.out.println(req2);
    }
}
