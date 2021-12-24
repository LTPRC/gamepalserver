package com.github.ltprc.entity.lasvegas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.github.ltprc.exception.BusinessException;
import com.github.ltprc.exception.ExceptionConstant;

@Component
public class Casino {
    /**
     * value, num
     */
    private TreeMap<Integer, Integer> banknoteMap = new TreeMap<>();

    /**
     * playerno, dicenum
     */
    private Map<Integer, Integer> diceMap = new HashMap<>();

    public TreeMap<Integer, Integer> getBanknoteMap() {
        return banknoteMap;
    }

    public Map<Integer, Integer> getDiceMap() {
        return diceMap;
    }

    public int getTotalValue() {
        int sum = 0;
        for (Entry entry : banknoteMap.entrySet()) {
            sum += (Integer) entry.getKey() * (Integer) entry.getValue();
        }
        return sum;
    }

    public void addBanknote(int value) {
        if (banknoteMap.containsKey(value)) {
            banknoteMap.put(value, banknoteMap.get(value) + 1);
        } else {
            banknoteMap.put(value, 1);
        }
    }

    public void substractBanknote(int value) throws BusinessException {
        if (!banknoteMap.containsKey(value)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_LV01);
        } else if (banknoteMap.get(value) > 1) {
            banknoteMap.put(value, banknoteMap.get(value) - 1);
        } else {
            banknoteMap.put(value, 0);
        }
    }

    public void addDice(int playerNo, int diceNum) {
        if (diceMap.containsKey(playerNo)) {
            diceMap.put(playerNo, diceMap.get(playerNo) + diceNum);
        } else {
            diceMap.put(playerNo, diceNum);
        }
    }

    public void clearDiceMap() {
        diceMap = new HashMap<>();
    }
}
