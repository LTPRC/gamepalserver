package com.github.ltprc.gamepal.model.map;

public class UserPosition extends Position {

    public UserPosition() {}

    public UserPosition(String uuid, Position position) {
        super();
        this.uuid = uuid;
        this.sceneNo = position.getSceneNo();
        this.x = position.getX();
        this.y = position.getY();
        this.speedX = position.getSpeedX();
        this.speedY = position.getSpeedY();
        this.direction = position.getDirection();
    }

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
