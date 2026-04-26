package com.shopnova.model;
public abstract class Person {
    public String name;
    public Person(String name) {
        this.name = name;
    }
    public abstract String toFile();
}
