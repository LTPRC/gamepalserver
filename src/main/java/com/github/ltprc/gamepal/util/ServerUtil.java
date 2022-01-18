package com.github.ltprc.gamepal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.model.VoiceMessage;
import com.github.ltprc.gamepal.model.map.Position;

@Component
public class ServerUtil {

    public final static String API_PATH = "/api/v1";
    public final static String WS_PATH = "/websocket/v1";
    public final static int MAX_MESSAGE_LINE_NUM = 10;
    public final static int MAX_CHAR_NUM_PER_LINE = 100;

    public final static Map<String, String> tokenMap = new ConcurrentHashMap<>(); // uuid, token
    public final static LinkedHashMap<String, Long> onlineMap = new LinkedHashMap<>(); // uuid, timestamp

    public final static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    public final static Map<String, UserData> userDataMap = new ConcurrentHashMap<>(); // uuid, userData
    public final static Map<Integer, Set<String>> userLocationMap = new ConcurrentHashMap<>(); // sceneNo, uuid
    public final static Map<String, Queue<ChatMessage>> chatMap = new ConcurrentHashMap<>(); // uuid, message queue
    public final static Map<String, Queue<VoiceMessage>> voiceMap = new ConcurrentHashMap<>(); // uuid, voice file queu

    /**
     * Deprecated
     */
    public final static Map<String, Position> positionMap = new ConcurrentHashMap<>(); // uuid, position

    /**
     * 集体发送消息
     * @param userCode
     * @param message
     */
    public static void sendMessageToAll(String message) {
        for (Entry<String, Session> entry : sessionMap.entrySet()) {
            entry.getValue().getAsyncRemote().sendText(message);
        }
    }

    public static String generateReplyContent(UserData userData) {
        String userCode = userData.getUserCode();
        JSONObject rst = new JSONObject();
        rst.put("userCode", userCode);
        rst.put("token", ServerUtil.tokenMap.get(userCode));
        List<Integer> sceneNos = new ArrayList<>();
        sceneNos.add(userData.getSceneNo());
        sceneNos.addAll(userData.getNearbySceneNos());
        Set<String> userCodes = new ConcurrentSkipListSet<>();
        for (int sceneNo : sceneNos) {
            userCodes.addAll(ServerUtil.userLocationMap.get(sceneNo));
        }
        /**
         * All nearby sceneNos will be included fFrom smaller y to bigger y.
         * Include userCode itself now!
         */
        Comparator<UserData> comparator = new Comparator<UserData>() {
            @Override
            public int compare(UserData up1, UserData up2) {
                return up1.getPlayerY().compareTo(up2.getPlayerY());
            }
        };
        Set<UserData> userDatas = new ConcurrentSkipListSet<>(comparator);
        for (String code : userCodes) {
            userDatas.add(ServerUtil.userDataMap.get(code));
        }
        rst.put("userDatas", JSON.toJSON(userDatas));

        if (ServerUtil.chatMap.containsKey(userCode) && !ServerUtil.chatMap.get(userCode).isEmpty()) {
            JSONArray chatMessages = new JSONArray();
            chatMessages.addAll(ServerUtil.chatMap.get(userCode));
            ServerUtil.chatMap.remove(userCode);
            rst.put("chatMessages", chatMessages);
        }

        if (ServerUtil.voiceMap.containsKey(userCode) && !ServerUtil.voiceMap.get(userCode).isEmpty()) {
            JSONArray voiceMessages = new JSONArray();
            voiceMessages.addAll(ServerUtil.voiceMap.get(userCode));
            ServerUtil.voiceMap.remove(userCode);
            rst.put("voiceMessages", voiceMessages);
        }

        return JSONArray.toJSONString(rst);
    }

    /**
     * 发送消息
     * @param userCode
     * @param message
     */
    public static void sendMessage(@PathParam("userCode") String userCode, String message) {
        // 向指定用户发送消息
        sessionMap.get(userCode).getAsyncRemote().sendText(message);
    }

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
