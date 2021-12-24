package com.github.ltprc.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.lang.NonNull;

import com.github.ltprc.entity.lasvegas.LasVegas;
import com.github.ltprc.exception.BusinessException;
import com.github.ltprc.exception.ExceptionConstant;

public class Server {

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
    private static List<Class<Subject>> subjectList = new ArrayList<>();
    @NonNull
    private static Map<String, Player> playerMap = new ConcurrentHashMap<>();

    /**
     * Initialization
     */
    static {
        try {
            addSubject(LasVegas.class);
            Room room = new Room();
            room.setName("test_room");
            LasVegas.addRoom(room);
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

    public static List<Class<Subject>> getSubjectList() {
        return subjectList;
    }

    public static Map<String, Player> getPlayerMap() {
        return playerMap;
    }

    public static boolean isLoggedIn(@NonNull HttpServletRequest request) throws BusinessException {
        HttpSession session = request.getSession(false);
        return null != session && sessionMap.containsKey(session.getId());
    }

    public static boolean hasSubject(@NonNull Class subjectClass) throws BusinessException {
        if (!Subject.class.isAssignableFrom(subjectClass)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1002);
        }
        return subjectList.contains(subjectClass);
    }


    public static boolean addSubject(@NonNull Class subjectClass) throws BusinessException {
        if (hasSubject(subjectClass)) {
            //Subject is already added.
            return false;
        }
        subjectList.add(subjectClass);
        return true;
    }

    /**
     * Only allows deleting the last element of subjectList.
     * @param subjectClass
     * @return
     * @throws BusinessException
     */
    public static boolean removeSubject(@NonNull Class subjectClass) throws BusinessException {
        if (!hasSubject(subjectClass)) {
            //Subject is not found.
            return false;
        } else if (subjectClass.equals(subjectList.get(subjectList.size() - 1))) {
            subjectList.remove(subjectClass);
            return true;
        } else {
            //Not allowed
            return false;
        }
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
