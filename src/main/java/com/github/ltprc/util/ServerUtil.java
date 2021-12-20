package com.github.ltprc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

public class ServerUtil {

    private static AtomicInteger online = new AtomicInteger(0);
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

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
