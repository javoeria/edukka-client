package com.javier.edukka.controllers;

import com.javier.edukka.models.ClassModel;

public class ClassSingleton {
    private static final ClassSingleton ourInstance = new ClassSingleton();
    private ClassModel model;

    private ClassSingleton() {}

    public static ClassSingleton getInstance() {
        return ourInstance;
    }

    public ClassModel getClassModel() {
        return model;
    }

    public void setClassModel(ClassModel model) {
        this.model = model;
    }
}
