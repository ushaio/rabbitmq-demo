package com.shai.invoke01;

/**
 * 反射目标类
 */
public class TargetObject {
    private String value;

    public TargetObject() {
        this.value = "JavaGuide";
    }

    public void publicMethod(String s) {
        System.out.println("hello public, I love " + s);
    }

    private void privateMethod() {
        System.out.println("hello private, value is " + value);
    }

    protected void protectedMethod() {
        System.out.println("hello protected, value is " + value);
    }
}