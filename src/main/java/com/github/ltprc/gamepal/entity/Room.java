package com.github.ltprc.gamepal.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.github.ltprc.gamepal.exception.BusinessException;
import com.github.ltprc.gamepal.exception.ExceptionConstant;
import com.github.ltprc.gamepal.entity.lasvegas.GameLasVegas;
import com.github.ltprc.gamepal.entity.lasvegas.SubjectLasVegas;

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

    public boolean createGame(Class<Subject> subjectClass, @NonNull String name) throws BusinessException {
        if (null != game) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1006);
        }
        if (!Server.hasSubject(subjectClass)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
        }
        if (subjectClass.equals(SubjectLasVegas.class)) {
            game = new GameLasVegas(Subject.class, name);
            return true;
        }
        return false;
    }
}
