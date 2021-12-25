package com.github.ltprc.gamepal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.gamepal.entity.Player;
import com.github.ltprc.gamepal.entity.Server;

@RestController
public class ProfileController {

    @RequestMapping("login")
    public String login(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        HttpSession session = request.getSession(false);
        if (null == session) {
            session = request.getSession(true);
            Player player = (Player) session.getAttribute("player");
            if (null == player || null == player.getHttpSession() || player.getHttpSession() != session) {
                player = new Player(session.getId(), session);
                session.setAttribute("player", player);
                Server.registerPlayer(player);
            }
            sb.append("Session:" + session.getId() + " has been successfully logged in.");
        } else {
            sb.append("Session:" + session.getId() + " has already been logged in.");
        }
        return sb.toString();
    }

    @RequestMapping("profile")
    public String profile(HttpServletRequest request) {
        if (!Server.isLoggedIn(request)) {
            return "You are not logged in!";
        }
        HttpSession session = request.getSession(false);
        Player player = (Player) session.getAttribute("player");
        return "Player:" + player.getName();
    }
}
