package com.aloha.thymeleaf.domain;

import lombok.Data;

@Data
public class Person {

    private String name;
    private int age;

    public Person() {
        this.name = "이름";
        this.age = 10;
    }
}