package com.github.ltprc.entity.lasvegas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Game;
import com.github.ltprc.entity.Subject;

@Component
public class GameLasVegas extends Game {

    /**
     * up to maximum
     */
    private int inning = 0;
    /**
     * up to 4
     */
    private int round = 0;
    /**
     * up to (playernum - 1)
     */
    private int turn = 0;
    /**
     * value, num
     */
    private Map<Integer, Integer> banknoteMap = new HashMap<>();
    private List<Integer> banknoteList = new ArrayList<>();
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
        banknoteMap.putAll(SubjectLasVegas.BANKNOTE_MAP);
        for (Entry entry : banknoteMap.entrySet()) {
            banknoteList.add((Integer) entry.getKey() * (Integer) entry.getValue());
        }
        for (int i = 0; i < banknoteList.size(); i++) {
            int temp = banknoteList.get(i);
            Random random = new Random(banknoteList.size());
            int rand = random.nextInt();
            banknoteList.set(i, banknoteList.get(rand));
            banknoteList.set(rand, temp);
        }
    }

    public GameLasVegas(Class<Subject> subjectClass, String name) {
        super(subjectClass, name);
    }

    public void updateStatus() {
        /**
         * Update turns.
         */
//        turn++;
        while (turn >= this.getPlayerNameSet().size()) {
            turn-= this.getPlayerNameSet().size();
            round++;
        }
        while (round >= SubjectLasVegas.ROUND_MAX_NUM) {
            round-= SubjectLasVegas.ROUND_MAX_NUM;
            inning++;
        }
        /**
         * Update cards.
         */
    }
}
