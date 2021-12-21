package com.github.ltprc.entity;

import org.springframework.stereotype.Component;

@Component
public class Player {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
