package com.shai.redis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 文档配置
 *
 * @Author: shai
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Redis 学习模块接口文档")
                .description("Redis 五大数据类型操作 + 缓存实战 + 分布式锁示例")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("shai")
                        .email("tech@example.com"));

        return new OpenAPI().info(info);
    }
}
