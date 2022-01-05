package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

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
public class ChatController {

    @RequestMapping(value = "/send-chat", method = RequestMethod.POST)
    public ResponseEntity<String> sendChat(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        Integer type;
        String uuid, receiver, content;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = body.get("uuid").toString();
            receiver = body.get("receiver").toString();
            type = (Integer) body.get("type");
            content = body.get("content").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message sending failed");
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUuid(uuid);
        chatMessage.setToUuid(receiver);
        chatMessage.setType(type);
        chatMessage.setContent(content);
        switch (type) {
        case 1:
            /**
             * Broadcasting
             */
            Set<Entry<String, Queue<ChatMessage>>> entrySet = new HashSet<>();
            entrySet.addAll(ServerUtil.chatMap.entrySet());
            for (Entry entry : entrySet) {
                if (!ServerUtil.chatMap.containsKey(entry.getKey())) {
                    ServerUtil.chatMap.put((String) entry.getKey(), new ConcurrentLinkedQueue<>());
                }
                ServerUtil.chatMap.get(entry.getKey()).add(chatMessage);
            }
            break;
        case 2:
            if (!ServerUtil.chatMap.containsKey(receiver)) {
                ServerUtil.chatMap.put(receiver, new ConcurrentLinkedQueue<>());
            }
            ServerUtil.chatMap.get(receiver).add(chatMessage);
            break;
        }
        return ResponseEntity.status(HttpStatus.OK).body("Message is sent");
    }

    @RequestMapping(value = "/receive-chat", method = RequestMethod.POST)
    public ResponseEntity<String> receiveChat(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        Integer type;
        String uuid, receiver, content;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = body.get("uuid").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message receiving failed");
        }
        if (ServerUtil.chatMap.containsKey(uuid)) {
            Queue<ChatMessage> queue = ServerUtil.chatMap.get(uuid);
            List<ChatMessage> list = new LinkedList<>();
            while (!queue.isEmpty()) {
                list.add(queue.poll());
            }
            rst.put("messages", list);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
