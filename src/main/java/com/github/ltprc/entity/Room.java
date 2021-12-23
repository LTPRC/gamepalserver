package com.github.ltprc.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.github.ltprc.entity.game.Game;
import com.github.ltprc.entity.game.lasvegas.GameLasVegas;
import com.github.ltprc.entity.subject.LasVegas;
import com.github.ltprc.exception.BusinessException;
import com.github.ltprc.exception.ExceptionConstant;
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

    public boolean createGame(int subjectIndex, @NonNull String name) throws BusinessException {
        if (null != game) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1006);
        }
        if (null == ServerUtil.getSubjectList().get(subjectIndex)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
        }
        if (ServerUtil.getSubjectList().get(subjectIndex).getClass().equals(LasVegas.class)) {
            game = new GameLasVegas(ServerUtil.getSubjectList().get(subjectIndex), name);
            return true;
        }
        return false;
    }
}
