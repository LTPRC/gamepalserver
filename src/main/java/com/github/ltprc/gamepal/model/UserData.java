package com.github.ltprc.gamepal.model;

import java.math.BigDecimal;
import java.util.List;

public class UserData {
    private String userCode;
    private List<Integer> nearbySceneNos;
    private int sceneNo;
    private BigDecimal playerX;
    private BigDecimal playerY;
    private int nextSceneNo;
    private BigDecimal playerNextX;
    private BigDecimal playerNextY;
    private BigDecimal playerSpeedX;
    private BigDecimal playerSpeedY;
    // Never exceed 1
    private BigDecimal playerMaxSpeedX;
    // Never exceed 1
    private BigDecimal playerMaxSpeedY;
    private BigDecimal acceleration;
    // 1-E 2-NE 3-N 4-NW 5-W 6-SW 7-S 8-SE
    private int playerDirection;

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
    private List<String> tools;
    private List<String> outfits;
    private int avatar;

    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
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
    public List<String> getTools() {
        return tools;
    }
    public void setTools(List<String> tools) {
        this.tools = tools;
    }
    public List<String> getOutfits() {
        return outfits;
    }
    public void setOutfits(List<String> outfits) {
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
}
