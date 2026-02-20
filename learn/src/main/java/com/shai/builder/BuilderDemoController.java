package com.shai.builder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Builder 模式演示 Controller
 *
 * <p>访问 GET /builder/demo 可以看到两种构建方式的输出：
 * <ul>
 *   <li>方式一：直接通过 {@code UserProfile.builder()} 链式构建</li>
 *   <li>方式二：通过 {@link UserProfile.UserProfileProperties} 从配置文件读取后转换</li>
 * </ul>
 */
@RestController
@RequestMapping("/builder")
public class BuilderDemoController {

    private final UserProfile.UserProfileProperties properties;

    public BuilderDemoController(UserProfile.UserProfileProperties properties) {
        this.properties = properties;
    }

    /**
     * 方式一：代码中直接使用 Builder 链式构建
     */
    @GetMapping("/manual")
    public UserProfile manual() {
        return UserProfile.builder()
                .username("shai")
                .email("shai@example.com")
                .age(18)
                .phone("13800000000")
                .address("Shanghai")
                .build();
    }

    /**
     * 方式二：从 application.yml 读取配置，通过 Builder 转换为不可变对象
     */
    @GetMapping("/from-config")
    public UserProfile fromConfig() {
        return properties.toUserProfile();
    }
}
