package com.github.ltprc.gamepal.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.model.UserStatus;
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
        String content = ServerUtil.generateInitContent(userCode);
        //ServerUtil.sendMessage(userCode, content);
//        System.out.println("Sent String: " + content);
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
    @OnMessage
    public void onMessage(@NonNull String message) {
//        System.out.println("Received String (size:" + message.length() + ")");
//        System.out.println("Received String:" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        if (null == jsonObject || !jsonObject.containsKey("userCode")) {
            return;
        }
        String userCode = jsonObject.getString("userCode").toString();
        if (jsonObject.containsKey("initFlag")) {
            String content = ServerUtil.generateInitContent(userCode);
            ServerUtil.sendMessage(userCode, content);
            return;
        }

        UserData userData = ServerUtil.userDataMap.get(userCode);
        UserStatus userStatus = ServerUtil.userStatusMap.get(userCode);
        /**
         * Update userDatas and userStatus
         */
        for (Entry<String, Object> entry : jsonObject.entrySet()) {
            switch (entry.getKey()) {
            case "nearbySceneNos":
                userData.setNearbySceneNos((List<Integer>) entry.getValue());
                break;
            case "sceneNo":
                userData.setSceneNo((int) entry.getValue());
                break;
            case "playerX":
                userData.setPlayerX(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerY":
                userData.setPlayerY(new BigDecimal(entry.getValue().toString()));
                break;
            case "nextSceneNo":
                userData.setNextSceneNo((int) entry.getValue());
                break;
            case "playerNextX":
                userData.setPlayerNextX(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerNextY":
                userData.setPlayerNextY(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerSpeedX":
                userData.setPlayerSpeedX(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerSpeedY":
                userData.setPlayerSpeedY(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerMaxSpeedX":
                userData.setPlayerMaxSpeedX(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerMaxSpeedY":
                userData.setPlayerMaxSpeedY(new BigDecimal(entry.getValue().toString()));
                break;
            case "acceleration":
                userData.setAcceleration(new BigDecimal(entry.getValue().toString()));
                break;
            case "playerDirection":
                userData.setPlayerDirection((int) entry.getValue());
                break;
            case "firstName":
                userData.setFirstName(entry.getValue().toString());
                break;
            case "lastName":
                userData.setLastName(entry.getValue().toString());
                break;
            case "nickname":
                userData.setNickname(entry.getValue().toString());
                break;
            case "nameColor":
                userData.setNameColor(entry.getValue().toString());
                break;
            case "creature":
                userData.setCreature(entry.getValue().toString());
                break;
            case "gender":
                userData.setGender(entry.getValue().toString());
                break;
            case "skinColor":
                userData.setSkinColor(entry.getValue().toString());
                break;
            case "hairstyle":
                userData.setHairstyle(entry.getValue().toString());
                break;
            case "hairColor":
                userData.setHairColor(entry.getValue().toString());
                break;
            case "eyes":
                userData.setEyes(entry.getValue().toString());
                break;
            case "outfit":
                userData.setOutfit(entry.getValue().toString());
                break;
            case "avatar":
                userData.setAvatar((int) entry.getValue());
                break;
            case "hpMax":
                userStatus.setHpMax((int) entry.getValue());
                break;
            case "hp":
                userStatus.setHp((int) entry.getValue());
                break;
            case "vpMax":
                userStatus.setVpMax((int) entry.getValue());
                break;
            case "vp":
                userStatus.setVp((int) entry.getValue());
                break;
            case "hungerMax":
                userStatus.setHungerMax((int) entry.getValue());
                break;
            case "hunger":
                userStatus.setHunger((int) entry.getValue());
                break;
            case "thirstMax":
                userStatus.setThirstMax((int) entry.getValue());
                break;
            case "thirst":
                userStatus.setThirst((int) entry.getValue());
                break;
            case "level":
                userStatus.setLevel((int) entry.getValue());
                break;
            case "exp":
                userStatus.setExp((int) entry.getValue());
                break;
            case "expMax":
                userStatus.setExpMax((int) entry.getValue());
                break;
            case "money":
                userStatus.setMoney((int) entry.getValue());
                break;
            case "items":
                JSONObject items = (JSONObject) entry.getValue();
                Map<String, Integer> iMap = userStatus.getItems();
                for (Entry<String, Object> bEntry : items.entrySet()) {
                    iMap.put(bEntry.getKey().toString(), (int) bEntry.getValue());
                }
                break;
            case "capacityMax":
                userStatus.setCapacityMax(new BigDecimal(entry.getValue().toString()));
                break;
            case "capacity":
                userStatus.setCapacity(new BigDecimal(entry.getValue().toString()));
                break;
            case "reservedItems":
                JSONObject reservedItems = (JSONObject) entry.getValue();
                Map<String, Integer> riMap = userStatus.getReservedItems();
                for (Entry<String, Object> bEntry : reservedItems.entrySet()) {
                    riMap.put(bEntry.getKey().toString(), (int) bEntry.getValue());
                }
                break;
            case "buff":
                userStatus.setBuff(entry.getValue().toString());
                /**
                 * Settle buff effect
                 */
                // To be continued...
                break;
            }
            /**
             * Check level-up
             */
            while (userStatus.getExp() >= userStatus.getExpMax()) {
                userStatus.setLevel(userStatus.getLevel() + 1);
                userStatus.setExp(userStatus.getExp() - userStatus.getExpMax());
                userStatus.setExpMax(100 * (int) Math.pow(userStatus.getLevel(), 2));
            }
            /**
             * Update UserLocation
             */
            if (jsonObject.containsKey("sceneNo") && userData.getSceneNo() != jsonObject.getInteger("sceneNo")) {
                Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(ServerUtil.userDataMap.get(userCode).getSceneNo(), new ConcurrentSkipListSet<>());
                userCodeSet.remove(userCode);
                ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(userCode).getSceneNo(), userCodeSet);
                userCodeSet = ServerUtil.userLocationMap.getOrDefault(jsonObject.getInteger("sceneNo"), new ConcurrentSkipListSet<>());
                userCodeSet.add(userCode);
                ServerUtil.userLocationMap.put(jsonObject.getInteger("sceneNo"), userCodeSet);
            }
            /**
             * Update userDataMap and userStatusMap
             */
            ServerUtil.userDataMap.put(userCode, userData);
            ServerUtil.userStatusMap.put(userCode, userStatus);
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
                        ServerUtil.chatMap.get(userDataEntry.getValue().getUserCode()).add(newMessage);
                    }
                    break;
                case 2:
                    ChatMessage newMessage = new ChatMessage();
                    newMessage.setFromUuid(userCode);
                    newMessage.setToUuid(receiver);
                    newMessage.setType(type);
                    newMessage.setContent(content);
                    ServerUtil.chatMap.get(receiver).add(newMessage);
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
                        ServerUtil.voiceMap.get(userDataEntry.getValue().getUserCode()).add(newMessage);
                    }
                    break;
                case 2:
                    VoiceMessage newMessage = new VoiceMessage();
                    newMessage.setFromUuid(userCode);
                    newMessage.setToUuid(receiver);
                    newMessage.setType(type);
                    newMessage.setContent(content);
                    ServerUtil.voiceMap.get(receiver).add(newMessage);
                    break;
                }
            }
        }

        String content = ServerUtil.generateReplyContent(userCode);
        ServerUtil.sendMessage(userCode, content);
    }

    private void afterLogoff(String userCode, String token) {
        List<UserOnline> userOnlineList = userOnlineRepository.queryUserOnlineByUuid(userCode);
        if (!userOnlineList.isEmpty()) {
            userOnlineRepository.delete(userOnlineList.get(0));
        }
        if (token.equals(ServerUtil.tokenMap.get(userCode))) {
            ServerUtil.tokenMap.remove(userCode);
            ServerUtil.onlineMap.remove(userCode);
            Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(ServerUtil.userDataMap.get(userCode).getSceneNo(), new ConcurrentSkipListSet<>());
            if (null != userCode) {
                userCodeSet.remove(userCode);
            }
            ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(userCode).getSceneNo(), userCodeSet);
            ServerUtil.chatMap.remove(userCode);
            ServerUtil.voiceMap.remove(userCode);
            ServerUtil.userDataMap.remove(userCode);
            ServerUtil.userStatusMap.remove(userCode);
        }
    }
}