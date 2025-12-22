package com.shai.serializable;

import lombok.Data;

import java.io.Serializable;


@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private int id;
}
