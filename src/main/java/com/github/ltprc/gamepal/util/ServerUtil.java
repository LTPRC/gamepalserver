package com.github.ltprc.gamepal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.model.map.Position;

public class ServerUtil {

    public final static String API_PATH = "/api/v1";

    public final static Map<String, Position> positionMap = new ConcurrentHashMap<>(); // uuid, position
    public final static Map<Integer, Set<String>> userLocationMap = new ConcurrentHashMap<>(); // sceneNo, uuid
    public final static Map<String, String> tokenMap = new ConcurrentHashMap<>(); // uuid, token

    public final static Map<String, Queue<ChatMessage>> chatMap = new ConcurrentHashMap<>(); // uuid, message queue

    public static JSONObject strRequest2JSONObject(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str = "";
        String listString = "";
        while ((str = br.readLine()) != null) {
            listString += str;
        }
       return (JSONObject) JSONObject.parse(listString);
    }
}
