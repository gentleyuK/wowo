package com.bestinyu.wowo.qiyeweixin;

import lombok.Data;

import java.util.List;

@Data
public class Department {
    private long id;
    private String name;
    private List<User> users;

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
