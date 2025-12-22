package com.shai.synchronizeddemo;

/**
 * 线程不安全的计数器示例
 */
public class Counter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}