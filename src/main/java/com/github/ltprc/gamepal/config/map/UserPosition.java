package com.github.ltprc.gamepal.config.map;

public class UserPosition extends Position {

    public UserPosition() {}

    public UserPosition(String uuid, Position position) {
        super();
        this.uuid = uuid;
        this.sceneNo = position.getSceneNo();
        this.x = position.getX();
        this.y = position.getY();
        this.outfit = position.getOutfit();
        this.speed = position.getSpeed();
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
