package com.github.ltprc.gamepal.model;

import java.math.BigDecimal;

public class Drop extends ClassicalPosition {

    protected String itemNo;
    protected int amount;

    public Drop() {
    }

    public Drop(int sceneNo, BigDecimal x, BigDecimal y, String itemNo, int amount) {
        super();
        this.sceneNo = sceneNo;
        this.x = x;
        this.y = y;
        this.itemNo = itemNo;
        this.amount = amount;
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

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
