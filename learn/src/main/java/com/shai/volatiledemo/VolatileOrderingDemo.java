package com.shai.volatiledemo;

/**
 * 演示 volatile 禁止指令重排序
 * 在多线程环境下，编译器和处理器可能会为了性能优化而对指令进行重排序。
 * volatile 通过内存屏障（Memory Barrier）来防止这种重排序。
 */
public class VolatileOrderingDemo {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (;;) {
            i++;
            x = 0; y = 0;
            a = 0; b = 0;

            Thread t1 = new Thread(() -> {
                // 如果不加 volatile，这两行代码的执行顺序可能会被重排
                a = 1;
                x = b;
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            /**
             * 正常执行逻辑下，x 和 y 不可能同时为 0。
             * 只有当 t1 中的 a=1 和 x=b 发生重排序，且 t2 中的 b=1 和 y=a 也发生重排序时，
             * 才可能出现 x=0 且 y=0 的情况。
             * 
             * 注意：由于现代 CPU 的优化非常复杂，这种现象可能需要运行很多次才能观察到。
             */
            if (x == 0 && y == 0) {
                System.err.println("第 " + i + " 次迭代出现了 x=0, y=0，说明发生了指令重排序！");
                System.err.println("如果给 a 和 b 加上 volatile 关键字，则不会出现这种情况。");
                break;
            }

            if (i % 100000 == 0) {
                System.out.println("已经运行了 " + i + " 次，尚未检测到重排序。");
            }
        }
    }
}
