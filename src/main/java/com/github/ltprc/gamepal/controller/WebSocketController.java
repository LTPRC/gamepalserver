package com.github.ltprc.gamepal.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
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

        UserData userData = ServerUtil.userDataMap.get(userCode);
        UserStatus userStatus = ServerUtil.userStatusMap.get(userCode);
        /**
         * Update userDatas and userStatus
         */
        if (jsonObject.containsKey("userData")) {
            JSONObject userDataObject = (JSONObject) jsonObject.get("userData");
            for (Entry<String, Object> entry : userDataObject.entrySet()) {
                switch (entry.getKey()) {
                case "nearbySceneNos":
                    userData.setNearbySceneNos((List<Integer>) entry.getValue());
                    break;
                case "sceneNo":
                    int newSceneNo = (int) entry.getValue();
                    /**
                     * Update UserLocation
                     */
                    if (userData.getSceneNo() != newSceneNo) {
                        userData.setSceneNo(newSceneNo);
                        Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(ServerUtil.userDataMap.get(userCode).getSceneNo(), new ConcurrentSkipListSet<>());
                        userCodeSet.remove(userCode);
                        ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(userCode).getSceneNo(), userCodeSet);
                        userCodeSet = ServerUtil.userLocationMap.getOrDefault(newSceneNo, new ConcurrentSkipListSet<>());
                        userCodeSet.add(userCode);
                        ServerUtil.userLocationMap.put(newSceneNo, userCodeSet);
                    }
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
                case "tools":
                    JSONArray toolsJSONArray = JSONArray.parseArray(entry.getValue().toString());
                    Set<String> tools = new HashSet<>();
                    for (Object str : toolsJSONArray) {
                        tools.add((String) str);
                    }
                    userData.setTools(tools);
                    break;
                case "outfits":
                    JSONArray outfitsJSONArray = JSONArray.parseArray(entry.getValue().toString());
                    Set<String> outfits = new HashSet<>();
                    for (Object str : outfitsJSONArray) {
                        outfits.add((String) str);
                    }
                    userData.setOutfits(outfits);
                    break;
                case "avatar":
                    userData.setAvatar(Integer.parseInt(entry.getValue().toString()));
                    break;
                }
            }
        }
        if (jsonObject.containsKey("userStatus")) {
            JSONObject userStatusObject = (JSONObject) jsonObject.get("userStatus");
            for (Entry<String, Object> entry : userStatusObject.entrySet()) {
                switch (entry.getKey()) {
                case "hpMax":
                    userStatus.setHpMax(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "hp":
                    userStatus.setHp(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "vpMax":
                    userStatus.setVpMax(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "vp":
                    userStatus.setVp(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "hungerMax":
                    userStatus.setHungerMax(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "hunger":
                    userStatus.setHunger(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "thirstMax":
                    userStatus.setThirstMax(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "thirst":
                    userStatus.setThirst(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "level":
                    userStatus.setLevel(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "exp":
                    userStatus.setExp(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "expMax":
                    userStatus.setExpMax(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "money":
                    userStatus.setMoney(Integer.parseInt(entry.getValue().toString()));
                    break;
                case "items":
                    JSONObject items = (JSONObject) entry.getValue();
                    Map<String, Integer> iMap = new HashMap<>();
                    for (Entry<String, Object> bEntry : items.entrySet()) {
                        iMap.put(bEntry.getKey().toString(), (int) bEntry.getValue());
                    }
                    userStatus.setItems(iMap);
                    break;
                case "capacityMax":
                    userStatus.setCapacityMax(new BigDecimal(entry.getValue().toString()));
                    break;
                case "capacity":
                    userStatus.setCapacity(new BigDecimal(entry.getValue().toString()));
                    break;
                case "preservedItems":
                    JSONObject preservedItems = (JSONObject) entry.getValue();
                    Map<String, Integer> riMap = new HashMap<>();
                    for (Entry<String, Object> bEntry : preservedItems.entrySet()) {
                        riMap.put(bEntry.getKey().toString(), (int) bEntry.getValue());
                    }
                    userStatus.setPreservedItems(riMap);
                    break;
                case "buff":
                    userStatus.setBuff(entry.getValue().toString());
                    /**
                     * Settle buff effect
                     */
                    // To be continued...
                    break;
                case "memberNumMax":
                    userStatus.setMemberNumMax((int) entry.getValue());
                    break;
    //            case "palCodeList": // Not here!
    //                JSONObject palCodeListJSON = (JSONObject) entry.getValue();
    //                List<String> palCodeList = new ArrayList<>();
    //                for (Entry<String, Object> pEntry : palCodeListJSON.entrySet()) {
    //                    palCodeList.add(pEntry.getValue().toString());
    //                }
    //                userStatus.setPalCodeList(palCodeList);
    //                break;
                case "palNumMax":
                    userStatus.setPalNumMax((int) entry.getValue());
                    break;
                }
            }
        }

        String content = ServerUtil.generateReplyContent(userCode);
        ServerUtil.sendMessage(userCode, content);
    }

    @Deprecated
    private void updateMessage(JSONObject jsonObject, String userCode) {
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
    }
}