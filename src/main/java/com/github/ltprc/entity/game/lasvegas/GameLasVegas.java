package com.github.ltprc.entity.game.lasvegas;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.game.Game;
import com.github.ltprc.entity.subject.Subject;

@Component
public class GameLasVegas extends Game {

    public GameLasVegas() {
        super();
    }

    public GameLasVegas(Subject subject, String name) {
        super(subject, name);
    }
}
