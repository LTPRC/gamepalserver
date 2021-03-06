package com.github.ltprc.gamepal.model;

import java.math.BigDecimal;

public class ClassicalPosition {

    protected int sceneNo;
    protected BigDecimal x;
    protected BigDecimal y;

    public ClassicalPosition() {
    }

    public ClassicalPosition(int sceneNo, BigDecimal x, BigDecimal y) {
        super();
        this.sceneNo = sceneNo;
        this.x = x;
        this.y = y;
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
}
