package com.github.ltprc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.entity.Player;
import com.github.ltprc.util.ServerUtil;

@RestController
public class ProfileController {

    @RequestMapping("login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (null == session) {
            session = request.getSession(true);
            return "Session:" + session.getId() + " has been successfully logged in.";
        } else {
            return "Session:" + session.getId() + " has already been logged in.";
        }
    }

    @RequestMapping("profile")
    public String profile(HttpServletRequest request) {
        if (!ServerUtil.isLoggedIn(request)) {
            return "You are not logged in!";
        }
        HttpSession session = request.getSession(false);
        Player player = (Player) session.getAttribute("player");
        if (null == player || null == player.getHttpSession() || player.getHttpSession() != session) {
            player = new Player(session.getId(), session);
            session.setAttribute("player", player);
            ServerUtil.registerPlayer(player);
        }
        return "Player:" + player.getName();
    }
}
