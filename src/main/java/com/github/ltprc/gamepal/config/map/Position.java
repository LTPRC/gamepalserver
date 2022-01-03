package com.github.ltprc.gamepal.config.map;

import java.math.BigDecimal;

public class Position {

    protected int sceneNo;
    protected BigDecimal x;
    protected BigDecimal y;
    protected int outfit;
    protected BigDecimal speed;
    protected int direction;

    public Position() {
    }

    public Position(int sceneNo, BigDecimal x, BigDecimal y, int outfit, BigDecimal speed, int direction) {
        super();
        this.sceneNo = sceneNo;
        this.x = x;
        this.y = y;
        this.outfit = outfit;
        this.speed = speed;
        this.direction = direction;
    }

    public int getSceneNo() {
        return sceneNo;
    }

    public void setSceneNo(int sceneNo) {
        this.sceneNo = sceneNo;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public int getOutfit() {
        return outfit;
    }

    public void setOutfit(int outfit) {
        this.outfit = outfit;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
