package com.github.ltprc.entity.lasvegas;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Game;
import com.github.ltprc.entity.Subject;

@Component
public class GameLasVegas extends Game {

    /**
     * up to maximum
     */
    private int inning = 0;
    /*
     * up to 4
     */
    private int round = 0;
    /*
     * up to playernum - 1
     */
    private int turn = 0;
    /**
     * value, num
     */
    private Map<Integer, Integer> banknoteMap = new HashMap<>();
    /**
     * index, casino
     */
    private Map<Integer, Casino> casinoMap = new HashMap<>();
    /**
     * playerno, value
     */
    private Map<Integer, Integer> valueMap = new HashMap<>();

    public GameLasVegas() {
        super();
        /**
         * Initialization
         */
        banknoteMap.putAll(LasVegas.BANKNOTE_MAP);
    }

    public GameLasVegas(Class<Subject> subjectClass, String name) {
        super(subjectClass, name);
    }
}
