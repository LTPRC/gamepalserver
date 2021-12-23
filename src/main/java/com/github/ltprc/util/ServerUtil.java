package com.github.ltprc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.lang.NonNull;

import com.github.ltprc.entity.LasVegas;
import com.github.ltprc.entity.Player;
import com.github.ltprc.entity.Subject;

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

    static {
        addSubject(LasVegas.class);
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

    public static boolean isLoggedIn(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return null != session && sessionMap.containsKey(session.getId());
    }

    /**
     * One instance per subject class.
     * @param subject
     */
    public static boolean addSubject(Class subjectClass) {
        if (null == subjectClass || !Subject.class.isAssignableFrom(subjectClass)) {
            return false;
        }
        for (Subject existedSubject : subjectList) {
            if (existedSubject.getClass().equals(subjectClass)) {
                return false;
            }
        }
        try {
            subjectList.add((Subject) subjectClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * One instance per subject class.
     * @param subject
     */
    public static boolean removeSubject(Class subjectClass) {
        if (null == subjectClass || !Subject.class.isAssignableFrom(subjectClass)) {
            return false;
        }
        for (int i = 0; i < subjectList.size(); i++) {
            if (subjectList.get(i).getClass().equals(subjectClass)) {
                subjectList.remove(i);
                return true;
            }
        }
        return false;
    }

    public static boolean registerPlayer(Player player) {
        if (null == player || null == player.getName() || playerMap.containsKey(player.getName()) 
                || playerMap.size() >= MAX_PLAYER_NUMBER) {
            return false;
        }
        playerMap.put(player.getName(), player);
        return true;
    }

    public static boolean deletePlayer(Player player) {
        if (null == player || null == player.getName() || !playerMap.containsKey(player.getName())) {
            return false;
        }
        playerMap.remove(player.getName());
        HttpSession httpSession = player.getHttpSession();
        if (null != httpSession) {
            httpSession.setAttribute("player", null);
        }
        return true;
    }
}
