package com.github.ltprc.gamepal.entity.lasvegas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.github.ltprc.gamepal.entity.Subject;
import com.github.ltprc.gamepal.exception.BusinessException;
import com.github.ltprc.gamepal.exception.ExceptionConstant;
import com.github.ltprc.gamepal.entity.Game;

@Component
public class GameLasVegas extends Game {

    public static final int STATUS_DICE_READY = 10;
    public static final int STATUS_DICE_DONE = 11;

    /**
     * up to maximum
     */
    private int inning = 0;
    /**
     * up to 4
     */
    private int round = 0;
    /**
     * up to (playerNum - 1)
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
    /**
     * playerno, diceNum
     */
    private Map<Integer, Integer> diceMap = new HashMap<>();

    public GameLasVegas() {
        super();
        /**
         * Initialization
         */
        banknoteMap.putAll(SubjectLasVegas.BANKNOTE_MAP);
        for (Entry<Integer, Integer> entry : banknoteMap.entrySet()) {
            banknoteList.add((Integer) entry.getKey() * (Integer) entry.getValue());
        }
        for (int i = 0; i < banknoteList.size(); i++) {
            int temp = banknoteList.get(i);
            Random random = new Random();
            int rand = random.nextInt(banknoteList.size());
            banknoteList.set(i, banknoteList.get(rand));
            banknoteList.set(rand, temp);
        }
        for (int i = 0; i < SubjectLasVegas.CASINO_NUM; i++) {
            casinoMap.put(i, new Casino());
        }
        for (int i = 0; i < getPlayerNameSet().size(); i++) {
            valueMap.put(i, 0);
            diceMap.put(i, SubjectLasVegas.DICE_NUM_PER_PLAYER);
        }
        setStatus(STATUS_DICE_READY);
    }

    public GameLasVegas(Class<Subject> subjectClass, String name) {
        super(subjectClass, name);
    }

    public void updateStatus() {
        /**
         * Update turns.
         */
        turn++;
        while (turn >= this.getPlayerNameSet().size()) {
            turn-= this.getPlayerNameSet().size();
            round++;
        }
        while (round >= SubjectLasVegas.ROUND_MAX_NUM) {
            round-= SubjectLasVegas.ROUND_MAX_NUM;
            inning++;
            /**
             * Settle banknotes
             */
            for (int i = 0; i < casinoMap.entrySet().size(); i++) {
                Casino casino = casinoMap.get(i);
                settle(casino.getBanknoteMap(), casino.getDiceMap());
            }
            /**
             * Update casinos.
             */
            for (int i = 0; i < casinoMap.entrySet().size(); i++) {
                Casino casino = casinoMap.get(i);
                while (casino.getTotalValue() < SubjectLasVegas.BANKNOTE_VALUE_PER_CASINO) {
                    if (banknoteList.isEmpty()) {
                        endGame();
                        return;
                    }
                    casino.addBanknote(banknoteList.get(banknoteList.size() - 1));
                    banknoteList.remove(banknoteList.size() - 1);
                }
                casino.clearDiceMap();
            }
            /**
             * Update dices.
             */
            valueMap = new HashMap<>();
            diceMap = new HashMap<>();
            for (int i = 0; i < getPlayerNameSet().size(); i++) {
                valueMap.put(i, 0);
                diceMap.put(i, SubjectLasVegas.DICE_NUM_PER_PLAYER);
            }
        }
    }

    public Map<Integer, Integer> dice(int playerNo, int diceNum) {
        if (turn != playerNo) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1013);
        }
        if (getStatus() != STATUS_DICE_READY) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1012);
        }
        Map<Integer, Integer> diceMap = new HashMap<>();
        if (diceNum == 0) {
            updateStatus();
            return diceMap;
        }
        for (int i = 0; i < diceNum; i++) {
            Random random = new Random();
            int num = random.nextInt(6);
            if (diceMap.containsKey(num)) {
                diceMap.put(num, diceMap.get(num) + 1);
            } else {
                diceMap.put(num, 1);
            }
        }
        setStatus(STATUS_DICE_DONE);
        return diceMap;
    }

    public void occupy(int playerNo, int casinoNo, int diceNum) {
        if (turn != playerNo) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1013);
        }
        if (getStatus() != STATUS_DICE_DONE) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1012);
        }
        if (casinoMap.containsKey(casinoNo)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_LV02);
        }
        if (diceMap.get(playerNo) < diceNum) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_LV03);
        }
        Casino casino = casinoMap.get(casinoNo);
        casino.addDice(playerNo, diceNum);
        diceMap.put(playerNo, diceMap.get(playerNo) - diceNum);
        setStatus(STATUS_DICE_READY);
        updateStatus();
    }

    public void settle(TreeMap<Integer, Integer> banknoteMap, Map<Integer, Integer> diceMap) {
        /**
         * dicenum, playerno
         */
        TreeMap<Integer, Integer> diceNumMap = new TreeMap<>();
        for (Entry<Integer, Integer> entry : diceMap.entrySet()) {
            if (!diceNumMap.containsKey(entry.getValue())) {
                diceNumMap.put(entry.getValue(), entry.getKey());
            } else {
                diceNumMap.put(entry.getValue(), -1);
            }
        }
        for (int i = diceNumMap.entrySet().size() - 1; i >= 0; i--) {
            if (banknoteMap.isEmpty() || diceNumMap.isEmpty()) {
                break;
            }
            int playerNo = diceNumMap.lastEntry().getValue();
            if (playerNo != -1) {
                int value = banknoteMap.lastKey();
                if (banknoteMap.get(value) <= 1) {
                    banknoteMap.remove(value);
                } else {
                    banknoteMap.put(value, banknoteMap.get(value) - 1);
                }
                valueMap.put(playerNo, valueMap.get(playerNo) + value);
            }
            diceNumMap.remove(diceNumMap.lastKey());
        }
    }

    public List<Integer> getWinners() {
        List<Integer> highestPlayerNo = new LinkedList<>();
        int highestPlayerValue = Integer.MIN_VALUE;
        for (Entry<Integer, Integer> entry : valueMap.entrySet()) {
            if (entry.getValue() > highestPlayerValue) {
                highestPlayerNo = new LinkedList<>();
                highestPlayerNo.add(entry.getKey());
            } else if (entry.getValue() == highestPlayerValue) {
                highestPlayerNo.add(entry.getKey());
            }
        }
        super.endGame();
        return highestPlayerNo;
    }
}
