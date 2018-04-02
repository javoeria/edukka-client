package com.javier.edukka.controllers;

import com.javier.edukka.models.UserModel;

public class UserSingleton {
    private static final UserSingleton ourInstance = new UserSingleton();
    private UserModel model;

    private UserSingleton() {}

    public static UserSingleton getInstance() {
        return ourInstance;
    }

    public UserModel getUserModel() {
        return model;
    }

    public void setUserModel(UserModel model) {
        this.model = model;
    }
}
