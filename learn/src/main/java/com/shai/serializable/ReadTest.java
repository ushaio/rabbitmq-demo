package com.shai.serializable;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * 反序列化，反序列化obj文件
 */

public class ReadTest {
    public static void main(String[] args) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.obj"));
        User user = (User) ois.readObject();
        ois.close();

        System.out.println("反序列化对象：" + user);
    }
}
