package com.github.ltprc.gamepal.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.model.VoiceMessage;
import com.github.ltprc.gamepal.repository.UserOnlineRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@Component
@ServerEndpoint(ServerUtil.WS_PATH + "/{userCode}")
public class WebSocketController {

    @Autowired
    UserOnlineRepository userOnlineRepository;

    private static final Log logger = LogFactory.getLog(WebSocketController.class);

    /**
     * 建立连接调用的方法
     * @param session
     * @param userCode
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userCode") String userCode) {
        ServerUtil.sessionMap.put(userCode, session);
        logger.info("建立连接成功");
    }

    /**
     * 关闭链接调用接口
     * @param userCode
     */
    @OnClose
    public void onClose(@PathParam("userCode") String userCode) {
        ServerUtil.sessionMap.remove(userCode);
        logger.info("断开连接成功");
    }

    /**
     * 接收消息
     * @param userCode
     * @param message
     */
    @SuppressWarnings("unchecked")
    @OnMessage
    public void onMessage(@NonNull String message) {
        System.out.println("Received String (size:" + message.length() + ")");
        System.out.println("Received String: " + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        if (null == jsonObject || !jsonObject.containsKey("userCode")) {
            return;
        }
        String userCode = jsonObject.getString("userCode").toString();

        UserData userData = ServerUtil.userDataMap.get(userCode);

        /**
         * Update userDatas
         */
        if (jsonObject.containsKey("userData")) {
            JSONObject newUserDataJSONObject = jsonObject.getJSONObject("userData");
            for (Entry<String, Object> entry : newUserDataJSONObject.entrySet()) {
                switch (entry.getKey()) {
                case "sceneNo":
                    userData.setSceneNo((int) entry.getValue());
                    break;
                case "nearbySceneNos":
                    userData.setNearbySceneNos((List<Integer>) entry.getValue());
                    break;
                case "playerX":
                    userData.setPlayerX((BigDecimal) entry.getValue());
                    break;
                case "playerY":
                    userData.setPlayerY((BigDecimal) entry.getValue());
                    break;
                case "playerNextX":
                    userData.setPlayerNextX((BigDecimal) entry.getValue());
                    break;
                case "playerNextY":
                    userData.setPlayerNextY((BigDecimal) entry.getValue());
                    break;
                case "playerSpeedX":
                    userData.setPlayerSpeedX((BigDecimal) entry.getValue());
                    break;
                case "playerSpeedY":
                    userData.setPlayerSpeedY((BigDecimal) entry.getValue());
                    break;
                case "playerMaxSpeedX":
                    userData.setPlayerMaxSpeedX((BigDecimal) entry.getValue());
                    break;
                case "playerMaxSpeedY":
                    userData.setPlayerMaxSpeedY((BigDecimal) entry.getValue());
                    break;
                case "acceleration":
                    userData.setAcceleration((BigDecimal) entry.getValue());
                    break;
                case "playerDirection":
                    userData.setPlayerDirection((int) entry.getValue());
                    break;
                case "firstName":
                    userData.setFirstName((String) entry.getValue());
                    break;
                case "lastName":
                    userData.setLastName((String) entry.getValue());
                    break;
                case "nickname":
                    userData.setNickname((String) entry.getValue());
                    break;
                case "nameColor":
                    userData.setNameColor((String) entry.getValue());
                    break;
                case "creature":
                    userData.setCreature((String) entry.getValue());
                    break;
                case "gender":
                    userData.setGender((String) entry.getValue());
                    break;
                case "skinColor":
                    userData.setSkinColor((String) entry.getValue());
                    break;
                case "hairstyle":
                    userData.setHairstyle((String) entry.getValue());
                    break;
                case "hairColor":
                    userData.setHairColor((String) entry.getValue());
                    break;
                case "eyes":
                    userData.setEyes((String) entry.getValue());
                    break;
                case "outfit":
                    userData.setOutfit((String) entry.getValue());
                    break;
                case "avatar":
                    userData.setAvatar((int) entry.getValue());
                    break;
                }
            }
            /**
             * Update UserLocation
             */
            if (userData.getSceneNo() != newUserDataJSONObject.getInteger("sceneNo")) {
                Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(ServerUtil.userDataMap.get(userCode).getSceneNo(), new ConcurrentSkipListSet<>());
                userCodeSet.remove(userCode);
                ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(userCode).getSceneNo(), userCodeSet);
                userCodeSet = ServerUtil.userLocationMap.getOrDefault(newUserDataJSONObject.getInteger("sceneNo"), new ConcurrentSkipListSet<>());
                userCodeSet.add(userCode);
                ServerUtil.userLocationMap.put(newUserDataJSONObject.getInteger("sceneNo"), userCodeSet);
            }
        }

        /**
         * Update ChatMessage
         */
        if (jsonObject.containsKey("chatMessages")) {
            JSONArray chatMessages = jsonObject.getJSONArray("chatMessages");
            for (Object chatMessage : chatMessages) {
                String receiver = ((JSONObject) chatMessage).get("receiver").toString();
                int type = (int) ((JSONObject) chatMessage).get("type");
                String content = ((JSONObject) chatMessage).get("content").toString();
                switch (type) {
                case 1:
                    /**
                     * Broadcasting
                     */
                    for (Entry<String, UserData> userDataEntry : ServerUtil.userDataMap.entrySet()) {
                        ChatMessage newMessage = new ChatMessage();
                        newMessage.setFromUuid(userCode);
                        newMessage.setToUuid(receiver);
                        newMessage.setType(type);
                        newMessage.setContent(content);
                        ServerUtil.chatMap.get(userCode).add(newMessage);
                    }
                    break;
                case 2:
                    ChatMessage newMessage = new ChatMessage();
                    newMessage.setFromUuid(userCode);
                    newMessage.setToUuid(receiver);
                    newMessage.setType(type);
                    newMessage.setContent(content);
                    ServerUtil.chatMap.get(userCode).add(newMessage);
                    break;
                }
            }
        }

        /**
         * Update VoiceMessage
         */
        if (jsonObject.containsKey("voiceMessages")) {
            JSONArray voiceMessages = jsonObject.getJSONArray("voiceMessages");
            for (Object voiceMessage : voiceMessages) {
                String receiver = ((JSONObject) voiceMessage).get("receiver").toString();
                int type = (int) ((JSONObject) voiceMessage).get("type");
                String content = ((JSONObject) voiceMessage).get("content").toString();
                switch (type) {
                case 1:
                    /**
                     * Broadcasting
                     */
                    for (Entry<String, UserData> userDataEntry : ServerUtil.userDataMap.entrySet()) {
                        VoiceMessage newMessage = new VoiceMessage();
                        newMessage.setFromUuid(userCode);
                        newMessage.setToUuid(receiver);
                        newMessage.setType(type);
                        newMessage.setContent(content);
                        ServerUtil.voiceMap.get(userCode).add(newMessage);
                    }
                    break;
                case 2:
                    VoiceMessage newMessage = new VoiceMessage();
                    newMessage.setFromUuid(userCode);
                    newMessage.setToUuid(receiver);
                    newMessage.setType(type);
                    newMessage.setContent(content);
                    ServerUtil.voiceMap.get(userCode).add(newMessage);
                    break;
                }
            }
        }

        ServerUtil.sendMessage(userCode, ServerUtil.generateReplyContent(userData));
    }

    private void afterLogoff(String uuid, String token) {
        List<UserOnline> userOnlineList = userOnlineRepository.queryUserOnlineByUuid(uuid);
        if (!userOnlineList.isEmpty()) {
            userOnlineRepository.delete(userOnlineList.get(0));
        }
        if (token.equals(ServerUtil.tokenMap.get(uuid))) {
            ServerUtil.tokenMap.remove(uuid);
            ServerUtil.onlineMap.remove(uuid);
            Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(ServerUtil.userDataMap.get(uuid).getSceneNo(), new ConcurrentSkipListSet<>());
            if (null != uuid) {
                userCodeSet.remove(uuid);
            }
            ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(uuid).getSceneNo(), userCodeSet);
            ServerUtil.chatMap.remove(uuid);
            ServerUtil.voiceMap.remove(uuid);
            ServerUtil.userDataMap.remove(uuid);
        }
    }
}