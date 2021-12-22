package com.github.ltprc.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public abstract class Subject {

    private String name;
    @NonNull
    private List<Room> roomList = new ArrayList<>();
    private int minPlayerNum = 1;
    private int maxPlayerNum = 1;
    private int maxRoomNum = 100;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public int getMinPlayerNum() {
        return minPlayerNum;
    }

    public void setMinPlayerNum(int minPlayerNum) {
        this.minPlayerNum = minPlayerNum;
    }

    public int getMaxPlayerNum() {
        return maxPlayerNum;
    }

    public void setMaxPlayerNum(int maxPlayerNum) {
        this.maxPlayerNum = maxPlayerNum;
    }

    public int getMaxRoomNum() {
        return maxRoomNum;
    }

    public void setMaxRoomNum(int maxRoomNum) {
        this.maxRoomNum = maxRoomNum;
    }
}
