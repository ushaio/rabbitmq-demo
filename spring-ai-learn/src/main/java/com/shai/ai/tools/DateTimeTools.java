package com.shai.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具 - 使用 @Tool 注解方式
 * Spring AI 1.0.0 推荐使用 @Tool 注解替代 @Bean Function 模式
 */
@Component
public class DateTimeTools {

    @Tool(description = "获取当前的日期、时间、星期几等信息。当用户询问今天日期、现在几点、今天星期几等问题时调用此函数")
    public DateTimeResponse getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        int dayOfWeek = now.getDayOfWeek().getValue(); // 1=Monday
        DateTimeResponse timeResponse = new DateTimeResponse(
                now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                now.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                weekDays[dayOfWeek - 1],
                now.getYear());
        System.out.println(timeResponse);
        return timeResponse;
    }

    public record DateTimeResponse(
            String date,
            String time,
            String dayOfWeek,
            int year
    ) {
    }
}
