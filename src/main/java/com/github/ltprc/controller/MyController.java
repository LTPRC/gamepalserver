package com.github.ltprc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.listener.MyHttpSessionListener;

@RestController
public class MyController {

    @RequestMapping("hello")
    public String hello(HttpSession session) {
        session.setAttribute("aa", "aa");
        return "Hello, springboot!";
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        return "login";
    }

    @RequestMapping("online")
    @ResponseBody
    public String online(){
        return "当前在线人数：" + MyHttpSessionListener.online + "人";
    }
}
