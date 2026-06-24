package com.shai.builder02;

import java.util.Date;

public class Person {
    private final String name; // required
    private final String sex; // required
    private final Date date; // required
    private final String email; // required

    private final int height; // optional
    private final String edu; // optional
    private final String nickName; // optional
    private final int weight; // optional
    private final String addr; // optional

    // 私有构造器，因此Person对象的创建必须依赖于Builder
    private Person(Builder builder) {
        this.name = builder.name;
        this.sex = builder.sex;
        this.date = builder.date;
        this.email = builder.email;
        this.height = builder.height;
        this.edu = builder.edu;
        this.nickName = builder.nickName;
        this.weight = builder.weight;
        this.addr = builder.addr;
    }

    public static class Builder {
        private final String name; // required，使用final修饰
        private final String sex; // required，使用final修饰
        private final Date date; // required，使用final修饰
        private final String email; // required，使用final修饰

        private int height; // optional，不使用final修饰
        private String edu; // optional，不使用final修饰
        private String nickName; // optional，不使用final修饰
        private int weight; // optional，不使用final修饰
        private String addr; // optional，不使用final修饰

        public Builder(String name, String sex, Date date, String email) {
            this.name = name;
            this.sex = sex;
            this.date = date;
            this.email = email;
        }

        // 返回Builder对象本身，链式调用
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        // 返回Builder对象本身，链式调用
        public Builder edu(String edu) {
            this.edu = edu;
            return this;
        }

        // 返回Builder对象本身，链式调用
        public Builder nickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        // 返回Builder对象本身，链式调用
        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        // 返回Builder对象本身，链式调用
        public Builder addr(String addr) {
            this.addr = addr;
            return this;
        }

        // 通过Builder构建所需Person对象，并且每次都产生新的Person对象
        public Person build() {
            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", date=" + date +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", edu='" + edu + '\'' +
                ", nickName='" + nickName + '\'' +
                ", weight=" + weight +
                ", addr='" + addr + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Person.Builder builder = new Person.Builder("rico", "boy", new Date(), "rico@tju.edu.cn");
        Person p1 = builder.height(173).addr("天津市").nickName("书呆子").build();
        System.out.println(p1.toString());
    }
}