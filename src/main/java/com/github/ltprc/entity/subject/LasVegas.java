package com.github.ltprc.entity.subject;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LasVegas extends Subject {

    private static final int CASINO_NUM = 6;
    private static final int DICE_NUM_PER_PLAYER = 8;
    //value (Unit:W) and its number
    private static final Map<Integer, Integer> BANKNOTE_MAP = new HashMap<>();
    private static final int BANKNOTE_VALUE_PER_CASINO = 5;

    public LasVegas() {
        super();
        setName("Las Vegas");
        setMinPlayerNum(1);
        setMaxPlayerNum(8);
        BANKNOTE_MAP.put(1, 6);
        BANKNOTE_MAP.put(2, 8);
        BANKNOTE_MAP.put(3, 8);
        BANKNOTE_MAP.put(4, 6);
        BANKNOTE_MAP.put(5, 6);
        BANKNOTE_MAP.put(6, 5);
        BANKNOTE_MAP.put(7, 5);
        BANKNOTE_MAP.put(8, 5);
        BANKNOTE_MAP.put(9, 5);
    }
}
