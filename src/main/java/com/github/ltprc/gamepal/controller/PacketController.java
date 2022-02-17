package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.ClassicalPosition;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class PacketController {

    @RequestMapping(value = "/get-packet", method = RequestMethod.POST)
    public ResponseEntity<String> getPacket(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            int sceneNo = Integer.parseInt(body.get("sceneNo").toString());
            int x = Integer.parseInt(body.get("x").toString());
            int y = Integer.parseInt(body.get("y").toString());
            Map<String, Integer> itemsMap = ServerUtil.packetMap.getOrDefault(new ClassicalPosition(sceneNo, x, y), new HashMap<>());
            rst.put("itemsMap", JSON.toJSON(itemsMap));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/set-packet", method = RequestMethod.POST)
    public ResponseEntity<String> setPacket(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            if (null == body || !body.containsKey("itemsMap")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            int sceneNo = Integer.parseInt(body.get("sceneNo").toString());
            int x = Integer.parseInt(body.get("x").toString());
            int y = Integer.parseInt(body.get("y").toString());
            JSONObject itemsObject = (JSONObject) JSONObject.parse((String) jsonObject.get("itemsMap"));
            Map<String, Integer> itemsMap = new HashMap<>();
            for (Entry<String, Object> itemEntry : itemsObject.entrySet()) {
                itemsMap.put(itemEntry.getKey(), Integer.parseInt(itemEntry.getValue().toString()));
            }
            ServerUtil.packetMap.put(new ClassicalPosition(sceneNo, x, y), itemsMap);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
