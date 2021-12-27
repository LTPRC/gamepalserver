package com.github.ltprc.gamepal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.gamepal.entity.Subject;
import com.github.ltprc.gamepal.model.Lobby;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.Game;
import com.github.ltprc.gamepal.entity.Server;

@RestController
@RequestMapping("/v1")
public class LobbyController {

    @RequestMapping("/lobby")
    @ResponseBody
    public String lobby(HttpServletRequest request) {
        Lobby rst = new Lobby();
        rst.setOnlinePlayerNum(Server.getOnline());
        rst.setOnlinePlayers(Server.getOnlinePlayers());
        rst.setSubjectNum(Server.getSubjectList().size());
        List<Class<Subject>> subjectList = Server.getSubjectList();
        List<String> subjects = new ArrayList<>();
        List<Integer> subjectRoomNums = new ArrayList<>();
        List<Integer> subjectRunningRoomNums = new ArrayList<>();
        for (Class<Subject> subjectClass : subjectList) {
            subjects.add(Subject.getSubjectName(subjectClass));
            subjectRoomNums.add(Subject.getRoomList(subjectClass).size());
            subjectRunningRoomNums.add((int) Subject.getRoomList(subjectClass)
                    .stream()
                    .filter(room -> null != room.getGame())
                    .filter(room -> Game.STATUS_RUNNING == room.getGame().getStatus())
                    .count());
        }
        String[] subjectsArray = new String[subjects.size()];
        int[] subjectRoomNumsArray = new int[subjects.size()];
        int[] subjectRunningRoomNumsArray = new int[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            subjectsArray[i] = subjects.get(i);
            subjectRoomNumsArray[i] = subjectRoomNums.get(i);
            subjectRunningRoomNumsArray[i] = subjectRunningRoomNums.get(i);
        }
        rst.setSubjects(subjectsArray);
        rst.setSubjectRoomNums(subjectRoomNumsArray);
        rst.setSubjectRunningRoomNums(subjectRunningRoomNumsArray);
        return JSONObject.toJSONString(rst);
    }
}
