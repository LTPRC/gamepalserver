package com.github.ltprc.gamepal.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltprc.gamepal.entity.Subject;
import com.github.ltprc.gamepal.exception.BusinessException;
import com.github.ltprc.gamepal.exception.ExceptionConstant;
import com.github.ltprc.gamepal.entity.Game;
import com.github.ltprc.gamepal.entity.Player;
import com.github.ltprc.gamepal.entity.Room;
import com.github.ltprc.gamepal.entity.Server;

@RestController
public class LobbyController {

    @RequestMapping("/lobby")
    @ResponseBody
    public String lobby(HttpServletRequest request) {
        List<Class<Subject>> subjectList = Server.getSubjectList();
        StringBuilder sb = new StringBuilder();
        sb.append("当前在线人数：" + Server.getOnline() + "人 ");
        sb.append("There ");
        sb.append(subjectList.size() <= 1 ? "is " : "are ");
        sb.append(subjectList.size());
        sb.append(subjectList.size() <= 1 ? " subject to be chosen. " : " subjects to be chosen. ");
        IntStream.range(0, subjectList.size()).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(Subject.getSubjectName(subjectList.get(index)));
            sb.append("(room*");
            sb.append(Subject.getRoomList(subjectList.get(index)).size());
            sb.append(") ");
        });
        return sb.toString();
    }

    @RequestMapping("/lobby/player-count")
    @ResponseBody
    public int playerCount(HttpServletRequest request) {
        return Server.getPlayerMap().size();
    }

    @RequestMapping("/lobby/online")
    @ResponseBody
    public int online(HttpServletRequest request) {
        return Server.getOnline();
    }

    @RequestMapping("/lobby/{subjectId}")
    @ResponseBody
    public String subject(HttpServletRequest request, @PathVariable("subjectId") String subjectId)
            throws BusinessException {
        Class<Subject> subjectClass = getSubject(subjectId);
        List<Room> roomList = Subject.getRoomList(subjectClass);

        StringBuilder sb = new StringBuilder();
        sb.append("Subject\"" + Subject.getSubjectName(subjectClass) + "\" ");
        sb.append("Room#:" + roomList.size() + " ");
        sb.append("There ");
        sb.append(roomList.size() <= 1 ? "is " : "are ");
        sb.append(roomList.size());
        sb.append(roomList.size() <= 1 ? " room to be chosen. " : " rooms to be chosen. ");
        IntStream.range(0, roomList.size()).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(roomList.get(index).getName());
            sb.append(" ");
        });
        return sb.toString();
    }

    private Class<Subject> getSubject(String subjectId) throws BusinessException {
        if (!StringUtils.isNumeric(subjectId)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1007);
        }
        int subjectIndex = Integer.valueOf(subjectId);
        List<Class<Subject>> subjectList = Server.getSubjectList();
        if (subjectIndex < 0 || subjectIndex >= subjectList.size()) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1008);
        }
        return subjectList.get(subjectIndex);
    }

    @RequestMapping("/lobby/{subjectId}/{roomId}")
    @ResponseBody
    public String room(HttpServletRequest request, @PathVariable("subjectId") String subjectId,
            @PathVariable("roomId") String roomId) throws BusinessException {
        Class subjectClass = getSubject(subjectId);
        Room room = getRoom(subjectId, roomId);
        Game game = room.getGame();

        StringBuilder sb = new StringBuilder();
        sb.append("Room\"" + room.getName() + "\" ");
        if (null == game) {
            sb.append("No game information.");
        } else {
            sb.append("Game\"" + game.getName() + "\"(" + game.getPlayerNameSet().size() + "/"
                    + Subject.getMaxPlayerNum(subjectClass) + ") ");
        }
        return sb.toString();
    }

    private Room getRoom(String subjectId, String roomId) throws BusinessException {
        Class subjectClass = getSubject(subjectId);
        if (!StringUtils.isNumeric(roomId)) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1009);
        }
        int roomIndex = Integer.valueOf(roomId);
        List<Room> roomList = Subject.getRoomList(subjectClass);
        if (roomIndex < 0 || roomIndex >= roomList.size()) {
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1010);
        }
        return roomList.get(roomIndex);
    }

    @RequestMapping("/lobby/{subjectId}/{roomId}/game")
    @ResponseBody
    public String game(HttpServletRequest request, @PathVariable("subjectId") String subjectId,
            @PathVariable("roomId") String roomId) {
        Class subjectClass = getSubject(subjectId);
        Room room = getRoom(subjectId, roomId);

        /**
         * Create game
         */
        StringBuilder sb = new StringBuilder();
        Game game = room.getGame();
        if (null == game) {
            room.createGame(subjectClass, "testgamename ");
            game = room.getGame();
        }

        /**
         * Enter game
         */
        Set<String> playerNameSet = game.getPlayerNameSet();
        Set<String> notReadyplayerNameSet = game.getNotReadyplayerNameSet();
        HttpSession session = request.getSession(false);
        Player player = (Player) session.getAttribute("player");
        if (playerNameSet.contains(player.getName())) {
//            sb.append("You have already entered. ");
        } else if (playerNameSet.size() < Subject.getMaxPlayerNum(subjectClass)) {
            playerNameSet.add(player.getName());
            notReadyplayerNameSet.add(player.getName());
//            sb.append("You have entered. ");
        } else {
//            sb.append("You have not entered since the room is full. ");
            throw new BusinessException(ExceptionConstant.ERROR_CODE_1011);
        }
        sb.append("Game\"" + game.getName() + "\"(" + playerNameSet.size() + "/" + Subject.getMaxPlayerNum(subjectClass)
                + ") ");
        IntStream.range(0, playerNameSet.size()).forEach(index -> {
            sb.append(index);
            sb.append(":");
            sb.append(playerNameSet.toArray()[index]);
            if (!notReadyplayerNameSet.contains(playerNameSet.toArray()[index])) {
                sb.append("[READY]");
            }
            sb.append(" ");
        });
        return sb.toString();
    }
}
