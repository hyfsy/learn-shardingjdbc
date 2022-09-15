package com.hyf.shardingsphere.basic.pojo;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class User {
    private Long   id;
    private String name;
    private String password;
    private String passwordPlain;
    private String passwordAssisted;

    public User(Long id, String name, String password, String passwordPlain, String passwordAssisted) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.passwordPlain = passwordPlain;
        this.passwordAssisted = passwordAssisted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", passwordPlain='" + passwordPlain + '\'' +
                ", passwordAssisted='" + passwordAssisted + '\'' +
                '}';
    }
}
