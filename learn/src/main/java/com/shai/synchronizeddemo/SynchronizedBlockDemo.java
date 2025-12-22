package com.shai.synchronizeddemo;

/**
 * synchronized代码块示例
 */
public class SynchronizedBlockDemo {
    private int count = 0;
    private final Object lock = new Object();

    // 使用synchronized代码块，锁对象是this
    public void incrementWithThis() {
        synchronized (this) {
            count++;
        }
    }

    // 使用synchronized代码块，锁对象是自定义对象
    public void incrementWithCustomLock() {
        synchronized (lock) {
            count++;
        }
    }

    // 使用synchronized代码块，锁对象是Class对象
    private static int staticCount = 0;

    public void incrementStaticWithClassLock() {
        synchronized (SynchronizedBlockDemo.class) {
            staticCount++;
        }
    }

    public int getCount() {
        synchronized (this) {
            return count;
        }
    }

    public static int getStaticCount() {
        synchronized (SynchronizedBlockDemo.class) {
            return staticCount;
        }
    }
}