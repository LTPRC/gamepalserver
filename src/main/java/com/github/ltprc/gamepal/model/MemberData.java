package com.github.ltprc.gamepal.model;

import java.util.Set;

@Deprecated
public class MemberData {
    private String memberCode;
    private String userCode;

    private String firstName;
    private String lastName;
    private String nickname;
    private String creature;
    private String gender;
    private String skinColor;
    private String hairstyle;
    private String hairColor;
    private String eyes;
    private Set<String> tools;
    private Set<String> outfits;
    private int avatar;

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
     * 等级
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

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreature() {
        return creature;
    }

    public void setCreature(String creature) {
        this.creature = creature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getHairstyle() {
        return hairstyle;
    }

    public void setHairstyle(String hairstyle) {
        this.hairstyle = hairstyle;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public Set<String> getTools() {
        return tools;
    }

    public void setTools(Set<String> tools) {
        this.tools = tools;
    }

    public Set<String> getOutfits() {
        return outfits;
    }

    public void setOutfits(Set<String> outfits) {
        this.outfits = outfits;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

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
}
