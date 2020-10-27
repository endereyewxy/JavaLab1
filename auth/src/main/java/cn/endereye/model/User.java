// Created by endereyewxy@gmail.com, 2020.10.13

package cn.endereye.model;

import cn.endereye.util.MD5;

public class User {
    private int id;

    private String username;

    private String password;

    private long serial;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getSerial() {
        return serial;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setPasswordRaw(String password) {
        this.password = MD5.md5(password);
        return this;
    }

    public User setSerial(long serial) {
        this.serial = serial;
        return this;
    }
}
