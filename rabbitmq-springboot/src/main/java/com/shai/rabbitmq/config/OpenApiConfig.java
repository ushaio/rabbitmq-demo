package com.shai.rabbitmq.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Author: shai
 * @CreateTime: 2025-11-23  14:25
 * @Version: 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * 配置 OpenAPI 文档元信息和全局设置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 1. 定义安全方案（如 JWT 认证，可选）
        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        // 2. 文档元信息（标题、版本、作者等）
        Info info = new Info()
                .title("Spring Boot 3.x 接口文档（OpenAPI 3.0）")
                .description("基于 SpringDoc OpenAPI 的接口文档，支持在线调试、自动生成 SDK")
                .version("v2.0.0") // 接口版本
                .contact(new Contact() // 联系人信息（可选）
                        .name("技术团队")
                        .email("tech@example.com")
                        .url("https://www.example.com"))
                .license(new License() // 许可证（可选）
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));

        // 3. 构建 OpenAPI 实例（整合元信息、安全方案）
        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("JWT")) // 全局启用 JWT 认证（可选）
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("JWT", jwtScheme));
    }
}
