package com.github.ltprc.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.github.ltprc.util.ServerUtil;

public class MyHttpSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("创建session");
        ServerUtil.addOnline(1);
        ServerUtil.getSessionMap().put(se.getSession().getId(), se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("销毁session");
        ServerUtil.addOnline(-1);
        ServerUtil.getSessionMap().remove(se.getSession().getId());
    }
}
