package com.github.ltprc.gamepal.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class UserData {
    private String userCode;
    private String masterCode; // Not in DB
    private int relationLevel; // Not in DB
    private int userType; // Not in DB

    private int worldNo; // Not in DB
    private List<Integer> nearbySceneNos;
    private int sceneNo;
    private BigDecimal playerX;
    private BigDecimal playerY;
    private int nextSceneNo;
    private BigDecimal playerNextX;
    private BigDecimal playerNextY;
    private BigDecimal playerSpeedX;
    private BigDecimal playerSpeedY;
    private BigDecimal playerMaxSpeedX; // Never exceed 1
    private BigDecimal playerMaxSpeedY; // Never exceed 1
    private BigDecimal acceleration;
    private int playerDirection; // 1-E 2-NE 3-N 4-NW 5-W 6-SW 7-S 8-SE

    private String firstName;
    private String lastName;
    private String nickname;
    private String nameColor;
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

    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getMasterCode() {
        return masterCode;
    }
    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }
    public int getRelationLevel() {
        return relationLevel;
    }
    public void setRelationLevel(int relationLevel) {
        this.relationLevel = relationLevel;
    }
    public int getUserType() {
        return userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }
    public int getWorldNo() {
        return worldNo;
    }
    public void setWorldNo(int worldNo) {
        this.worldNo = worldNo;
    }
    public int getSceneNo() {
        return sceneNo;
    }
    public void setSceneNo(int sceneNo) {
        this.sceneNo = sceneNo;
    }
    public List<Integer> getNearbySceneNos() {
        return nearbySceneNos;
    }
    public void setNearbySceneNos(List<Integer> nearbySceneNos) {
        this.nearbySceneNos = nearbySceneNos;
    }
    public BigDecimal getPlayerX() {
        return playerX;
    }
    public void setPlayerX(BigDecimal playerX) {
        this.playerX = playerX;
    }
    public BigDecimal getPlayerY() {
        return playerY;
    }
    public void setPlayerY(BigDecimal playerY) {
        this.playerY = playerY;
    }
    public BigDecimal getPlayerNextX() {
        return playerNextX;
    }
    public void setPlayerNextX(BigDecimal playerNextX) {
        this.playerNextX = playerNextX;
    }
    public BigDecimal getPlayerNextY() {
        return playerNextY;
    }
    public void setPlayerNextY(BigDecimal playerNextY) {
        this.playerNextY = playerNextY;
    }
    public BigDecimal getPlayerSpeedX() {
        return playerSpeedX;
    }
    public void setPlayerSpeedX(BigDecimal playerSpeedX) {
        this.playerSpeedX = playerSpeedX;
    }
    public BigDecimal getPlayerSpeedY() {
        return playerSpeedY;
    }
    public void setPlayerSpeedY(BigDecimal playerSpeedY) {
        this.playerSpeedY = playerSpeedY;
    }
    public BigDecimal getPlayerMaxSpeedX() {
        return playerMaxSpeedX;
    }
    public void setPlayerMaxSpeedX(BigDecimal playerMaxSpeedX) {
        this.playerMaxSpeedX = playerMaxSpeedX;
    }
    public BigDecimal getPlayerMaxSpeedY() {
        return playerMaxSpeedY;
    }
    public void setPlayerMaxSpeedY(BigDecimal playerMaxSpeedY) {
        this.playerMaxSpeedY = playerMaxSpeedY;
    }
    public BigDecimal getAcceleration() {
        return acceleration;
    }
    public void setAcceleration(BigDecimal acceleration) {
        this.acceleration = acceleration;
    }
    public int getPlayerDirection() {
        return playerDirection;
    }
    public void setPlayerDirection(int playerDirection) {
        this.playerDirection = playerDirection;
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
    public String getNameColor() {
        return nameColor;
    }
    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
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
    public int getNextSceneNo() {
        return nextSceneNo;
    }
    public void setNextSceneNo(int nextSceneNo) {
        this.nextSceneNo = nextSceneNo;
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
