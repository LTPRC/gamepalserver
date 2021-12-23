package com.github.ltprc.entity.game.lasvegas;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.game.Game;
import com.github.ltprc.entity.subject.Subject;

@Component
public class GameLasVegas extends Game {

    private int round = 0;
    private int turn = 0;

    public GameLasVegas() {
        super();
    }

    public GameLasVegas(Class<Subject> subjectClass, String name) {
        super(subjectClass, name);
    }
}
