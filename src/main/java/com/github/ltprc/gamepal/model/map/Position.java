package com.github.ltprc.gamepal.model.map;

import java.math.BigDecimal;

public class Position {

    protected int sceneNo;
    protected BigDecimal x;
    protected BigDecimal y;
    protected BigDecimal speedX;
    protected BigDecimal speedY;
    protected int direction;

    public Position() {
    }

    public Position(int sceneNo, BigDecimal x, BigDecimal y, BigDecimal speedX, BigDecimal speedY, int direction) {
        super();
        this.sceneNo = sceneNo;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
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

    public BigDecimal getSpeedX() {
        return speedX;
    }

    public void setSpeedX(BigDecimal speedX) {
        this.speedX = speedX;
    }

    public BigDecimal getSpeedY() {
        return speedY;
    }

    public void setSpeedY(BigDecimal speedY) {
        this.speedY = speedY;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
