package com.github.ltprc.entity.lasvegas;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Subject;

@Component
public class SubjectLasVegas extends Subject {

    public static final int CASINO_NUM = 6;
    public static final int DICE_NUM_PER_PLAYER = 8;
    //value (Unit:W) and its number
    public static final Map<Integer, Integer> BANKNOTE_MAP = new HashMap<>();
    public static final int BANKNOTE_VALUE_PER_CASINO = 5;
    public static final int ROUND_MAX_NUM = 4;

    public SubjectLasVegas() {
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
