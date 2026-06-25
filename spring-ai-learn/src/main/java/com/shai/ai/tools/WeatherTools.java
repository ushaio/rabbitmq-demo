package com.shai.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 天气查询工具 - 使用 @Tool 注解方式
 * 演示带参数的函数调用：AI 会从用户问题中提取城市名作为参数
 */
@Component
public class WeatherTools {

    @Tool(description = "查询指定城市的当前天气信息，包括温度、湿度、天气状况和风力。当用户询问某地天气时调用此函数")
    public WeatherResponse getWeather(@ToolParam(description = "城市名称，如北京、上海、广州") String city) {
        // 模拟不同城市的天气数据（实际项目中应调用真实天气 API）
        Map<String, WeatherResponse> mockData = new HashMap<>();
        mockData.put("北京", new WeatherResponse("北京", "晴", 28, 45, "北风3级"));
        mockData.put("上海", new WeatherResponse("上海", "多云", 26, 65, "东南风2级"));
        mockData.put("广州", new WeatherResponse("广州", "小雨", 32, 80, "南风2级"));
        mockData.put("深圳", new WeatherResponse("深圳", "阴", 30, 75, "西南风3级"));
        mockData.put("杭州", new WeatherResponse("杭州", "晴转多云", 27, 55, "东风2级"));
        mockData.put("成都", new WeatherResponse("成都", "阴", 24, 70, "微风"));
        mockData.put("武汉", new WeatherResponse("武汉", "晴", 31, 50, "北风2级"));

        if (mockData.containsKey(city)) {
            return mockData.get(city);
        }

        // 随机生成天气数据（演示用）
        String[] weathers = {"晴", "多云", "阴", "小雨", "大雨", "雷阵雨"};
        String[] winds = {"北风", "南风", "东风", "西风", "东南风", "西北风"};
        Random random = new Random();

        return new WeatherResponse(
                city,
                weathers[random.nextInt(weathers.length)],
                15 + random.nextInt(20),
                30 + random.nextInt(50),
                winds[random.nextInt(winds.length)] + (1 + random.nextInt(4)) + "级"
        );
    }

    public record WeatherResponse(
            String city,
            String weather,
            int temperature,
            int humidity,
            String wind
    ) {}
}
