package com.shai.volatiledemo;

import java.util.concurrent.TimeUnit;

/**
 * 演示 volatile 的可见性
 * volatile 保证了不同线程对该变量操作的内存可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
 */
public class VolatileVisibilityDemo {
    // 尝试将 volatile 去掉，观察程序是否能正常退出
    private static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 正在运行...");
            while (flag) {
                // 如果没有 volatile，这里可能会进入死循环，因为线程缓存了 flag 的旧值
            }
            System.out.println(Thread.currentThread().getName() + " 检测到 flag 变为 false，退出循环。");
        }, "ReaderThread").start();

        try {
            // 等待 1 秒，确保 ReaderThread 已经开始运行并进入循环
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main 线程准备将 flag 修改为 false...");
        flag = false;
        System.out.println("Main 线程已将 flag 修改为 false。");
    }
}
