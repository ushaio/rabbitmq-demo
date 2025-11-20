package com.shai.invoke01;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 使用反射获取并操作TargetObject
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 获取 TargetObject 类的 Class 对象并且创建 TargetObject 类实例
        Class<?> targetClass = Class.forName("com.shai.invoke01.TargetObject");
        TargetObject targetObject = (TargetObject) targetClass.newInstance();

        // 获取 TargetObject 类中定义的所有方法
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        // 获取指定方法并调用
        Method publicMethod = targetClass.getDeclaredMethod("publicMethod",
                String.class);

        publicMethod.invoke(targetObject, "JavaGuide");

        // 获取指定参数并对参数进行修改
        Field field = targetClass.getDeclaredField("value");
        // 为了对类中的参数进行修改我们取消安全检查
        field.setAccessible(true);
        field.set(targetObject, "JavaGuide-SetValue"); // value is JavaGuide-SetValue

        // 调用 private 方法
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
        // 为了调用private方法我们取消安全检查
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetObject);

        Method protectedMethod = targetClass.getDeclaredMethod("protectedMethod");
        protectedMethod.invoke(targetObject);
    }
}