package com.hyf.shardingsphere.entity;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
public class User {
    private Long userId;
    private String  name;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
