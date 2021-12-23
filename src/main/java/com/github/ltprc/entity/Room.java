package com.github.ltprc.entity;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.game.Game;
import com.github.ltprc.entity.game.lasvegas.GameLasVegas;
import com.github.ltprc.util.ServerUtil;

@Component
public class Room {

    private String name;
    private Game game;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean createGame(int subjectIndex) {
        if (null != game || null == ServerUtil.getSubjectList().get(subjectIndex)) {
            return false;
        }
        if (ServerUtil.getSubjectList().get(subjectIndex).getClass().equals(LasVegas.class)) {
            game = new GameLasVegas(ServerUtil.getSubjectList().get(subjectIndex));
            return true;
        }
        return false;
    }
}
