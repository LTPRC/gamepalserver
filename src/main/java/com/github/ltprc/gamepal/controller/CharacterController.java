package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserCharacter;
import com.github.ltprc.gamepal.model.ChatMessage;
import com.github.ltprc.gamepal.repository.UserCharacterRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class CharacterController {

    @Autowired
    UserCharacterRepository userCharacterRepository;

    @RequestMapping(value = "/get-user-character", method = RequestMethod.POST)
    public ResponseEntity<String> receiveChat(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid;
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
        List<UserCharacter> characters = userCharacterRepository.queryUserCharacterByUuid(uuid);
        rst.put("characters", characters);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
