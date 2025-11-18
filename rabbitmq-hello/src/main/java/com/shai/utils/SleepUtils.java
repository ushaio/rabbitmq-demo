package com.shai.utils;

/**
 * @Description: TODO
 * @Author: shai
 * @CreateTime: 2025-11-16  15:13
 * @Version: 1.0
 */
public class SleepUtils {
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
