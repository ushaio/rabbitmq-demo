package com.shai.volatiledemo;

/**
 * 演示 volatile 不保证原子性
 * volatile 仅保证可见性，不保证复合操作（如 i++）的原子性。
 */
public class VolatileAtomicityDemo {
    private static volatile int count = 0;

    public static void increment() {
        count++; // 这是一个复合操作：读取、加一、写入。volatile 不能保证这三个步骤的原子性。
    }

    public static void main(String[] args) throws InterruptedException {
        int threadsCount = 10;
        int iterations = 1000;
        Thread[] threads = new Thread[threadsCount];

        /**
         * 创建10个线程，每个线程执行 1000 次 count++ 操作
         */
        for (int i = 0; i < threadsCount; i++) {
            // 创建线程
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    increment();
                }
            });
            // 启动线程
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread t : threads) {
            t.join(); // 等待线程结束
        }

        // 预期结果应该是 threadsCount * iterations (10000)
        // 但由于 volatile 不保证原子性，实际结果往往小于 10000
        System.out.println("最终 count 值: " + count);
        if (count < threadsCount * iterations) {
            System.out.println("结果证明 volatile 不保证原子性。");
        } else {
            System.out.println("本次运行恰好结果正确，请多次尝试。");
        }
    }
}
