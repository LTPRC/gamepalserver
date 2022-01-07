package com.github.ltprc.gamepal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.model.map.Position;
import com.github.ltprc.gamepal.repository.UserOnlineRepository;

public class ServerUtil {

    public final static String API_PATH = "/api/v1";

    public final static Map<String, String> tokenMap = new ConcurrentHashMap<>(); // uuid, token
    public final static LinkedHashMap<String, Long> onlineMap = new LinkedHashMap<>(); // uuid, timestamp
    public final static Map<String, Position> positionMap = new ConcurrentHashMap<>(); // uuid, position
    public final static Map<Integer, Set<String>> userLocationMap = new ConcurrentHashMap<>(); // sceneNo, uuid
    public final static Map<String, Queue<ChatMessage>> chatMap = new ConcurrentHashMap<>(); // uuid, message queue

    @Autowired
    static UserOnlineRepository userOnlineRepository;

    public static JSONObject strRequest2JSONObject(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str = "";
        String listString = "";
        while ((str = br.readLine()) != null) {
            listString += str;
        }
       return (JSONObject) JSONObject.parse(listString);
    }

    public static void afterLogoff(String uuid, String token) {
        afterLogoff(uuid);
        if (token.equals(ServerUtil.tokenMap.get(uuid))) {
            ServerUtil.tokenMap.remove(uuid);
        }
    }

    public static void afterLogoff(String uuid) {
        List<UserOnline> userOnlineList = userOnlineRepository.queryUserOnlineByUuid(uuid);
        if (!userOnlineList.isEmpty()) {
            userOnlineRepository.delete(userOnlineList.get(0));
        }
//        if (ServerUtil.positionMap.containsKey(uuid)) {
//            Position position = ServerUtil.positionMap.get(uuid);
//            int sceneNo = position.getSceneNo();
//            if (ServerUtil.userLocationMap.containsKey(sceneNo)) {
//                Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
//                uuidSet.remove(uuid);
//            }
//        }
        ServerUtil.chatMap.remove(uuid, new ConcurrentLinkedQueue<>());
        ServerUtil.onlineMap.remove(uuid);
    }
}
