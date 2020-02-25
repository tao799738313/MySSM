package com.pdt.ssm.bean;

public class User {
    private String USER_NAME;
    private String USER_EMAIL;
    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getUSER_EMAIL() {
        return USER_EMAIL;
    }

    public void setUSER_EMAIL(String USER_EMAIL) {
        this.USER_EMAIL = USER_EMAIL;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    @Override
    public String toString() {
        return "User{" +
                "USER_NAME='" + USER_NAME + '\'' +
                ", USER_EMAIL='" + USER_EMAIL + '\'' +
                '}';
    }
}
