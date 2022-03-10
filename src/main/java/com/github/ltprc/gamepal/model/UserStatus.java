package com.github.ltprc.gamepal.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UserStatus {

    /**
     * 金钱
     */
    private int money;
    /**
     * 物品 (itemId, amount)
     */
    private Map<String, Integer> items = new HashMap<>();
    /**
     * 背包容量
     */
    private BigDecimal capacity;
    /**
     * 背包容量最大值
     */
    private BigDecimal capacityMax;
    /**
     * 储备物品 (itemId, amount)
     */
    private Map<String, Integer> preservedItems = new HashMap<>();
    /**
     * 特殊状态，例如"000100011"
     */
    private String buff;
    /**
     * 成员数量上限
     */
    private int memberNumMax;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getCapacityMax() {
        return capacityMax;
    }

    public void setCapacityMax(BigDecimal capacityMax) {
        this.capacityMax = capacityMax;
    }

    public String getBuff() {
        return buff;
    }

    public void setBuff(String buff) {
        this.buff = buff;
    }

    public Map<String, Integer> getPreservedItems() {
        return preservedItems;
    }

    public void setPreservedItems(Map<String, Integer> preservedItems) {
        this.preservedItems = preservedItems;
    }

    public int getMemberNumMax() {
        return memberNumMax;
    }

    public void setMemberNumMax(int memberNumMax) {
        this.memberNumMax = memberNumMax;
    }
}
