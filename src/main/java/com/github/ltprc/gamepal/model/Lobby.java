package com.github.ltprc.gamepal.model;

public class Lobby {

    private int onlinePlayerNum;
    private String[] onlinePlayers;
    private int subjectNum;
    private String[] subjects;
    private int[] subjectRoomNums;
    private int[] subjectRunningRoomNums;

    public int getOnlinePlayerNum() {
        return onlinePlayerNum;
    }

    public void setOnlinePlayerNum(int onlinePlayerNum) {
        this.onlinePlayerNum = onlinePlayerNum;
    }

    public String[] getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(String[] onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getSubjectNum() {
        return subjectNum;
    }

    public void setSubjectNum(int subjectNum) {
        this.subjectNum = subjectNum;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public int[] getSubjectRoomNums() {
        return subjectRoomNums;
    }

    public void setSubjectRoomNums(int[] subjectRoomNums) {
        this.subjectRoomNums = subjectRoomNums;
    }

    public int[] getSubjectRunningRoomNums() {
        return subjectRunningRoomNums;
    }

    public void setSubjectRunningRoomNums(int[] subjectRunningRoomNums) {
        this.subjectRunningRoomNums = subjectRunningRoomNums;
    }
}
