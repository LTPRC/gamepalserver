package com.github.ltprc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.lang.NonNull;

import com.github.ltprc.entity.Player;
import com.github.ltprc.entity.Room;
import com.github.ltprc.entity.subject.LasVegas;
import com.github.ltprc.entity.subject.Subject;
import com.github.ltprc.exception.BusinessException;
import com.github.ltprc.exception.ExceptionConstant;

public class ServerUtil {

    private static final int MAX_PLAYER_NUMBER = 100;

    /**
     * Server Components
     */
    private static AtomicInteger online = new AtomicInteger(0);
    @NonNull
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Business Components
     */
    @NonNull
    private static List<Subject> subjectList = new ArrayList<>();
    @NonNull
    private static Map<String, Player> playerMap = new ConcurrentHashMap<>();

    /**
     * Initialization
     */
    static {
        try {
            addSubject(LasVegas.class);
            Subject subject = getSubject(LasVegas.class);
            Room room = new Room();
            room.setName("test_room");
            subject.addRoom(room);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int getOnline() {
        return online.get();
    }

    public static void addOnline(int num) {
        System.out.println("addOnline by " + num);
        online.addAndGet(num);
    }

    public static Map<String, HttpSession> getSessionMap() {
        return sessionMap;
    }

    public static List<Subject> getSubjectList() {
        return subjectList;
    }

    public static Map<String, Player> getPlayerMap() {
        return playerMap;
    }

    public static boolean isLoggedIn(@NonNull HttpServletRequest request) throws BusinessException {
        HttpSession session = request.getSession(false);
        return null != session && sessionMap.containsKey(session.getId());
    }

    /**
     * One instance per subject class.
     * @param subject
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static boolean addSubject(@NonNull Class subjectClass) throws BusinessException {
        if (!Subject.class.isAssignableFrom(subjectClass)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1002);
        }
        for (Subject existedSubject : subjectList) {
            if (existedSubject.getClass().equals(subjectClass)) {
                //Subject is already added.
                return false;
            }
        }
        try {
            subjectList.add((Subject) subjectClass.newInstance());
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1004);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1004);
        }
        return true;
    }

    /**
     * One instance per subject class.
     * @param subject
     */
    public static boolean removeSubject(@NonNull Class subjectClass) throws BusinessException {
        if (!Subject.class.isAssignableFrom(subjectClass)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1002);
        }
        for (int i = 0; i < subjectList.size(); i++) {
            if (subjectList.get(i).getClass().equals(subjectClass)) {
                subjectList.remove(i);
                return true;
            }
        }
        //Subject is not found.
        return false;
    }

    public static Subject getSubject(@NonNull Class subjectClass) throws BusinessException {
        for (int i = 0; i < subjectList.size(); i++) {
            if (subjectList.get(i).getClass().equals(subjectClass)) {
                return subjectList.get(i);
            }
        }
        throw new BusinessException(ExceptionConstant.ERROR_CODE_1003);
    }

    public static boolean registerPlayer(@NonNull Player player) {

        if (playerMap.containsKey(player.getName())) {
            //Already registered
            return true;
        }
        if (playerMap.size() >= MAX_PLAYER_NUMBER) {
            //Lobby is full
            return false;
        }
        playerMap.put(player.getName(), player);
        return true;
    }

    public static boolean deletePlayer(@NonNull String playerName) {
        if (!playerMap.containsKey(playerName)) {
            return false;
        }
        playerMap.remove(playerName);
        Player player = playerMap.get(playerName);
        if (null != player) {
            HttpSession httpSession = player.getHttpSession();
            if (null != httpSession) {
                httpSession.setAttribute("player", null);
            }
        }
        return true;
    }
}
