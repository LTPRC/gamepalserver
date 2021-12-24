package com.github.ltprc.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Room;
import com.github.ltprc.entity.lasvegas.SubjectLasVegas;
import com.github.ltprc.exception.BusinessException;
import com.github.ltprc.exception.ExceptionConstant;

@Component
public abstract class Subject {

    private static String name;
    @NonNull
    private static List<Room> roomList = new ArrayList<>();
    private static int minPlayerNum = 1;
    private static int maxPlayerNum = 1;
    private static int maxRoomNum = 100;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Subject.name = name;
    }

    public static List<Room> getRoomList() {
        return roomList;
    }

    public static void setRoomList(List<Room> roomList) {
        Subject.roomList = roomList;
    }

    public static int getMinPlayerNum() {
        return minPlayerNum;
    }

    public static void setMinPlayerNum(int minPlayerNum) {
        Subject.minPlayerNum = minPlayerNum;
    }

    public static int getMaxPlayerNum() {
        return maxPlayerNum;
    }

    public static void setMaxPlayerNum(int maxPlayerNum) {
        Subject.maxPlayerNum = maxPlayerNum;
    }

    public static int getMaxRoomNum() {
        return maxRoomNum;
    }

    public static void setMaxRoomNum(int maxRoomNum) {
        Subject.maxRoomNum = maxRoomNum;
    }

    public static boolean addRoom(@NonNull Room room) {
        if (roomList.size() >= maxRoomNum) {
            return false;
        }
        roomList.add(room);
        return true;
    }

    public static boolean removeRoom(int index) {
        if (roomList.isEmpty() || index < 0 || index >= maxRoomNum) {
            return false;
        }
        roomList.remove(index);
        return true;
    }

    public static String getName(Class<Subject> subjectClass) throws BusinessException {
        if (subjectClass.equals(SubjectLasVegas.class)) {
            return SubjectLasVegas.getName();
        }
        throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
    }

    public static List<Room> getRoomList(Class<Subject> subjectClass) throws BusinessException {
        if (subjectClass.equals(SubjectLasVegas.class)) {
            return SubjectLasVegas.getRoomList();
        }
        throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
    }

    public static int getMaxPlayerNum(Class<Subject> subjectClass) throws BusinessException {
        if (subjectClass.equals(SubjectLasVegas.class)) {
            return SubjectLasVegas.getMaxPlayerNum();
        }
        throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
    }

    public static int getMinPlayerNum(Class<Subject> subjectClass) throws BusinessException {
        if (subjectClass.equals(SubjectLasVegas.class)) {
            return SubjectLasVegas.getMinPlayerNum();
        }
        throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
    }
}
