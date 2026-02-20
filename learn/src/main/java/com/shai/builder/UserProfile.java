package com.shai.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Builder 模式优化版（Spring Boot 场景）
 *
 * <p>
 * 核心要点：
 * <ul>
 * <li>{@code @Builder} —— Lombok 自动生成静态内部 Builder 类，无需手写链式方法</li>
 * <li>{@code @ToString} —— 自动生成 toString，无需手写</li>
 * <li>字段全部 {@code final}，对象不可变</li>
 * <li>通过 {@link UserProfileProperties} 从 application.yml 读取配置，再用 Builder
 * 构建实例</li>
 * </ul>
 *
 * <p>
 * application.yml 示例：
 * 
 * <pre>
 * demo:
 *   user:
 *     username: shai
 *     email: shai@example.com
 *     age: 18
 *     phone: "13800000000"
 *     address: Shanghai
 * </pre>
 */
@Builder
@Getter
@ToString
public class UserProfile {

    /** 用户名（必填） */
    private final String username;

    /** 邮箱（必填） */
    private final String email;

    /** 年龄（可选） */
    private final Integer age;

    /** 手机号（可选） */
    private final String phone;

    /** 地址（可选） */
    private final String address;

    // -----------------------------------------------------------------------
    // Spring Boot 配置属性绑定（prefix = "demo.user"）
    // -----------------------------------------------------------------------

    /**
     * 从 application.yml 读取 demo.user.* 配置，
     * 通过 {@link #toUserProfile()} 转换为不可变的 {@link UserProfile} 实例。
     *
     * <p>
     * 注意：{@code @ConfigurationProperties} 需要 setter，
     * 因此这里单独用一个可变的 Properties 类承接配置，
     * 再通过 Builder 转换为不可变对象，职责清晰。
     */
    @Component
    @ConfigurationProperties(prefix = "demo.user")
    @lombok.Data
    public static class UserProfileProperties {

        private String username;
        private String email;
        private Integer age;
        private String phone;
        private String address;

        /**
         * 将配置属性转换为不可变的 {@link UserProfile} 实例。
         */
        public UserProfile toUserProfile() {
            return UserProfile.builder()
                    .username(username)
                    .email(email)
                    .age(age)
                    .phone(phone)
                    .address(address)
                    .build();
        }
    }
}
