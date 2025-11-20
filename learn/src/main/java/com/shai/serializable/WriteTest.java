package com.shai.serializable;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化操作，生成序列化的obj文件
 */
public class WriteTest {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("Alice");
        // user.setId(1);
        // user.setAge(12);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.obj"));
        oos.writeObject(user);
        oos.close();

        System.out.println("序列化完成");
    }
}
