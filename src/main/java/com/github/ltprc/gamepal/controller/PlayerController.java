package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class PlayerController {

    @RequestMapping(value = "/set-relation", method = RequestMethod.POST)
    public ResponseEntity<String> setDrop(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            if (null == body) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            String userCode = body.get("userCode").toString();
            String nextUserCode = body.get("nextUserCode").toString();
            int newRelation = Integer.parseInt(body.get("newRelation").toString());
            if (!ServerUtil.enemyMap.containsKey(userCode)) {
                ServerUtil.enemyMap.put(userCode, new HashMap<>());
            }
            if (!ServerUtil.enemyMap.containsKey(nextUserCode)) {
                ServerUtil.enemyMap.put(nextUserCode, new HashMap<>());
            }
            ServerUtil.enemyMap.get(userCode).put(nextUserCode, newRelation);
            ServerUtil.enemyMap.get(nextUserCode).put(userCode, newRelation);
            /**
             * Send feedback messages to both players.
             */
            ChatMessage chatMessage1 = getRelationMessage(userCode, nextUserCode, true, newRelation);
            ServerUtil.chatMap.get(userCode).add(chatMessage1);
            ChatMessage chatMessage2 = getRelationMessage(userCode, nextUserCode, false, newRelation);
            ServerUtil.chatMap.get(nextUserCode).add(chatMessage2);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    private ChatMessage getRelationMessage(String userCode, String nextUserCode, boolean isFrom, int newRelation) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUuid(userCode);
        chatMessage.setType(2);
        String content = new String();
        if (isFrom) {
            chatMessage.setToUuid(userCode);
            switch (newRelation) {
            case -1:
                content = "你向" + ServerUtil.userDataMap.get(nextUserCode).getNickname() + "发动了攻击！";
                break;
            case 0:
                content = "你向" + ServerUtil.userDataMap.get(nextUserCode).getNickname() + "表示了好感。";
                break;
            }
        } else {
            chatMessage.setToUuid(nextUserCode);
            switch (newRelation) {
            case -1:
                content = ServerUtil.userDataMap.get(userCode).getNickname() + "向你发动了攻击！";
                break;
            case 0:
                content = ServerUtil.userDataMap.get(userCode).getNickname() + "向你表示了好感。";
                break;
            }
        }
        chatMessage.setContent(content);
        return chatMessage;
    }
}
