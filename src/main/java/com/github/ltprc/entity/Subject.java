package com.github.ltprc.entity;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public abstract class Subject {

    private String name;
    private List<Room> roomList;

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
}
