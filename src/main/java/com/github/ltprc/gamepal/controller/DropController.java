package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.github.ltprc.gamepal.model.Drop;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class DropController {

    @RequestMapping(value = "/get-drop", method = RequestMethod.POST)
    public ResponseEntity<String> getDrop(HttpServletRequest request) {
        return getDropSync(request);
    }

    private synchronized ResponseEntity<String> getDropSync(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            int dropNo = Integer.parseInt(body.get("dropNo").toString());
            if (!ServerUtil.dropMap.containsKey(dropNo)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            Drop drop = ServerUtil.dropMap.get(dropNo);
            if (!ServerUtil.userStatusMap.containsKey(userCode)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            Map<String, Integer> items = ServerUtil.userStatusMap.get(userCode).getItems();
            if (null == items) {
                ServerUtil.userStatusMap.get(userCode).setItems(new HashMap<>());
            }
            int difference = Integer.MAX_VALUE - drop.getAmount();
            if (difference < items.getOrDefault(drop.getItemNo(), 0)) {
                items.put(drop.getItemNo(), Integer.MAX_VALUE);
                drop.setAmount(drop.getAmount() - difference);
                ServerUtil.dropMap.put(dropNo, drop);
            } else {
                items.put(drop.getItemNo(), items.get(drop.getItemNo()) + drop.getAmount());
                ServerUtil.dropMap.remove(dropNo);
                while (ServerUtil.dropNo > ServerUtil.DROP_NO_MIN && !ServerUtil.dropMap.containsKey(ServerUtil.dropNo - 1)) {
                    ServerUtil.dropNo--;
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/set-drop", method = RequestMethod.POST)
    public ResponseEntity<String> setDrop(HttpServletRequest request) {
        return setDropSync(request);
    }

    private synchronized ResponseEntity<String> setDropSync(HttpServletRequest request) {
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
            String itemNo = body.get("itemNo").toString();
            int amount = Integer.parseInt(body.get("amount").toString());
            Drop drop = new Drop(sceneNo, new BigDecimal(x + Math.random()), new BigDecimal(y + Math.random()), itemNo, amount);
            ServerUtil.dropMap.put(ServerUtil.dropNo, drop);
            if (ServerUtil.dropNo < Integer.MAX_VALUE) {
                /**
                 * The last drop will be erased.
                 */
                ServerUtil.dropNo++;
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
