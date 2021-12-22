package com.github.ltprc.controller;

import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.entity.Game;
import com.github.ltprc.entity.Room;
import com.github.ltprc.entity.Subject;
import com.github.ltprc.util.ServerUtil;

@RestController
public class LobbyController {

    @RequestMapping("/lobby")
    @ResponseBody
    public String lobby(HttpServletRequest request) {
        List<Subject> subjectList = ServerUtil.getSubjectList();
        StringBuilder sb = new StringBuilder();
        sb.append("当前在线人数：" + ServerUtil.getOnline() + "人 ");
        sb.append("There ");
        sb.append(subjectList.size() <= 1 ? "is " : "are ");
        sb.append(subjectList.size());
        sb.append(subjectList.size() <= 1 ? " subject to be chosen. " : " subjects to be chosen. ");
        IntStream.range(0, subjectList.size() - 1).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(subjectList.get(index).getName());
            sb.append("(room*");
            sb.append(subjectList.get(index).getRoomList().size());
            sb.append(") ");
        });
        return sb.toString();
    }

    @RequestMapping("/lobby/player-count")
    @ResponseBody
    public int playerCount(HttpServletRequest request) {
        return ServerUtil.getPlayerMap().size();
    }

    @RequestMapping("/lobby/online")
    @ResponseBody
    public int online(HttpServletRequest request) {
        return ServerUtil.getOnline();
    }

    @RequestMapping("/lobby/{subjectId}")
    @ResponseBody
    public String subject(HttpServletRequest request, @PathVariable("subjectId") String subjectId) {
        if (!StringUtils.isNumeric(subjectId)) {
            return "This subject id is illegal!";
        }
        int subjectIndex = Integer.valueOf(subjectId);
        List<Subject> subjectList = ServerUtil.getSubjectList();
        if (subjectIndex < 0 || subjectIndex >= subjectList.size()) {
            return "This subject does not exist!";
        }
        Subject subject = subjectList.get(subjectIndex);
        List<Room> roomList = subject.getRoomList();
        StringBuilder sb = new StringBuilder();
        sb.append("Subject\""+subject.getName()+"\" ");
        sb.append("Room#:" + roomList.size() + " ");
        sb.append("There ");
        sb.append(roomList.size() <= 1 ? "is " : "are ");
        sb.append(roomList.size());
        sb.append(roomList.size() <= 1 ? " room to be chosen. " : " rooms to be chosen. ");
        IntStream.range(0, roomList.size() - 1).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(roomList.get(index).getName());
            sb.append(" ");
        });
        return sb.toString();
    }
    
    @RequestMapping("/lobby/{subjectId}/{roomId}")
    @ResponseBody
    public String room(HttpServletRequest request, @PathVariable("subjectId") String subjectId, @PathVariable("roomId") String roomId) {
        if (!StringUtils.isNumeric(subjectId)) {
            return "This subject id is illegal!";
        }
        int subjectIndex = Integer.valueOf(subjectId);
        List<Subject> subjectList = ServerUtil.getSubjectList();
        if (subjectIndex < 0 || subjectIndex >= subjectList.size()) {
            return "This subject does not exist!";
        }
        Subject subject = subjectList.get(subjectIndex);
        
        if (!StringUtils.isNumeric(roomId)) {
            return "This subject id is illegal!";
        }
        int roomIndex = Integer.valueOf(roomId);
        List<Room> roomList = subject.getRoomList();
        if (roomIndex < 0 || roomIndex >= roomList.size()) {
            return "This room does not exist!";
        }
        Room room = roomList.get(roomIndex);
        Game game = room.getGame();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Room\""+room.getName()+"\" ");
        sb.append("Game\""+game.getName()+"\"("+ game.getPlayerNameSet().size() + "/" + subject.getMaxPlayerNum() + ") ");
        sb.append("Room#:" + roomList.size() + " ");
        sb.append("There ");
        sb.append(roomList.size() <= 1 ? "is " : "are ");
        sb.append(roomList.size());
        sb.append(roomList.size() <= 1 ? " room to be chosen. " : " rooms to be chosen. ");
        IntStream.range(0, roomList.size() - 1).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(roomList.get(index).getName());
            sb.append(" ");
        });
        return sb.toString();
    }
}
