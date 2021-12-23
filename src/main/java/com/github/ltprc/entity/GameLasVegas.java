package com.github.ltprc.entity;

import org.springframework.stereotype.Component;

@Component
public class GameLasVegas extends Game {

    public GameLasVegas() {
        super();
    }

    public GameLasVegas(Subject subject) {
        setSubject(subject);
    }
}
