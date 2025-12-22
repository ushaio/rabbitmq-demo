package com.shai.synchronizeddemo;

/**
 * 银行账户示例 - 演示synchronized在实际场景中的应用
 */
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // synchronized方法保证存款操作的线程安全
    public synchronized void deposit(double amount) {
        if (amount > 0) {
            System.out.println(Thread.currentThread().getName() + " 存款前余额: " + balance);
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " 存入: " + amount + "，存款后余额: " + balance);
        }
    }

    // synchronized方法保证取款操作的线程安全
    public synchronized boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " 取款前余额: " + balance);
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " 取出: " + amount + "，取款后余额: " + balance);
            return true;
        }
        System.out.println(Thread.currentThread().getName() + " 余额不足，无法取出: " + amount);
        return false;
    }

    public synchronized double getBalance() {
        return balance;
    }
}
