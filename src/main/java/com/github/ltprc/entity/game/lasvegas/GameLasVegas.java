package com.github.ltprc.entity.game.lasvegas;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Subject;
import com.github.ltprc.entity.game.Game;

@Component
public class GameLasVegas extends Game {

    public GameLasVegas() {
        super();
    }

    public GameLasVegas(Subject subject) {
        setSubject(subject);
    }
}
