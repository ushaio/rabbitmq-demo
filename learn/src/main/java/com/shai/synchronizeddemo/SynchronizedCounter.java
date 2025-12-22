package com.shai.synchronizeddemo;

/**
 * 使用synchronized方法实现线程安全的计数器
 */
public class SynchronizedCounter {
    private int count = 0;

    // synchronized修饰实例方法，锁对象是this
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }

    // synchronized修饰静态方法，锁对象是Class对象
    private static int staticCount = 0;

    public static synchronized void incrementStatic() {
        staticCount++;
    }

    public static synchronized int getStaticCount() {
        return staticCount;
    }
}