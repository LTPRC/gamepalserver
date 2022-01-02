package com.github.ltprc.gamepal.config.map;

public class Position {

    protected int sceneNo;
    protected int x;
    protected int y;
    protected int outfit;
    protected int speed;
    protected int direction;

    public Position() {
    }

    public Position(int sceneNo, int x, int y, int outfit, int speed, int direction) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOutfit() {
        return outfit;
    }

    public void setOutfit(int outfit) {
        this.outfit = outfit;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
