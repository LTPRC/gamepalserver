package com.github.ltprc.gamepal.model;

import java.util.HashMap;
import java.util.Map;

public class UserStatus {
    /**
     * 生命值最大值
     */
    private int hpMax;
    /**
     * 生命值
     */
    private int hp;
    /**
     * 活力值最大值
     */
    private int vpMax;
    /**
     * 活力值
     */
    private int vp;
    /**
     * 饥饿值
     */
    private int hunger;
    /**
     * 饥饿值最大值
     */
    private int hungerMax;
    /**
     * 口渴值
     */
    private int thirst;
    /**
     * 口渴值最大值
     */
    private int thirstMax;
    /**
     * 等级活力值
     */
    private int level;
    /**
     * 经验值
     */
    private int exp;
    /**
     * 经验值最大值
     */
    private int expMax;
    /**
     * 金钱
     */
    private int money;
    /**
     * 物品 (belongingsId, amount)
     */
    private Map<Integer, Integer> belongings = new HashMap<>();

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getVpMax() {
        return vpMax;
    }

    public void setVpMax(int vpMax) {
        this.vpMax = vpMax;
    }

    public int getVp() {
        return vp;
    }

    public void setVp(int vp) {
        this.vp = vp;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHungerMax() {
        return hungerMax;
    }

    public void setHungerMax(int hungerMax) {
        this.hungerMax = hungerMax;
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public int getThirstMax() {
        return thirstMax;
    }

    public void setThirstMax(int thirstMax) {
        this.thirstMax = thirstMax;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getExpMax() {
        return expMax;
    }

    public void setExpMax(int expMax) {
        this.expMax = expMax;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Map<Integer, Integer> getBelongings() {
        return belongings;
    }

    public void setBelongings(Map<Integer, Integer> belongings) {
        this.belongings = belongings;
    }
}
