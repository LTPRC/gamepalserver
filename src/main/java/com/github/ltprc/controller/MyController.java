package com.github.ltprc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.util.ServerUtil;

@RestController
public class MyController {

    @RequestMapping("hello")
    public String hello(HttpSession session) {
        session.setAttribute("aa", "aa");
        return "Hello, springboot!";
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (null == session) {
            session = request.getSession(true);
            return session.getId() + " has been successfully logged in.";
        } else {
            return session.getId() + " has already been logged in.";
        }
    }

    @RequestMapping("online")
    @ResponseBody
    public int online() {
        return ServerUtil.getOnline();
    }

    @RequestMapping("lobby")
    @ResponseBody
    public String lobby() {
        return "当前在线人数：" + ServerUtil.getOnline() + "人";
    }
}
