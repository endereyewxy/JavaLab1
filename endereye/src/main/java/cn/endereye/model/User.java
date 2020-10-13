package cn.endereye.model;

import cn.endereye.util.MD5;

public class User {
    public final String username;

    public final String passwordMd5;

    public final String passwordRaw;

    public User(String username, String password) {
        this.username = username;
        this.passwordMd5 = MD5.md5(password);
        this.passwordRaw = password;
    }
}
