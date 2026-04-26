package com.shopnova.model;
public abstract class User {
    public String username;
    public String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public abstract String toFile();
    public abstract String getRole();
}
