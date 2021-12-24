package com.github.ltprc.entity.lasvegas;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class Casino {
    /**
     * value, num
     */
    private Map<Integer, Integer> banknoteMap = new HashMap<>();
    /**
     * playerno, dicenum
     */
    private Map<Integer, Integer> diceMap = new HashMap<>();
    
    public int getTotalValue() {
        int sum = 0;
        for (Entry entry : banknoteMap.entrySet()) {
            sum += (Integer) entry.getKey() * (Integer) entry.getValue();
        }
        return sum;
    }
}
