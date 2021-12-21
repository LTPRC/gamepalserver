package com.github.ltprc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import com.github.ltprc.entity.Room;
import com.github.ltprc.entity.Subject;

public class ServerUtil {

    /**
     * Server Components
     */
    private static AtomicInteger online = new AtomicInteger(0);
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Business Components
     */
    private static List<Subject> subjectList = new ArrayList<>();

    public static int getOnline() {
        return online.get();
    }

    public static void addOnline(int num) {
        online.addAndGet(num);
    }
    
    public static Map<String, HttpSession> getSessionMap() {
        return sessionMap;
    }
}
