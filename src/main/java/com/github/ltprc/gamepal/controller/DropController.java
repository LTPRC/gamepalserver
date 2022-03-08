package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
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
            if (!ServerUtil.userStatusMap.containsKey(userCode)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            String dropNo = body.get("dropNo").toString();
            if (!ServerUtil.dropMap.containsKey(dropNo)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            Drop drop = ServerUtil.dropMap.get(dropNo);
            rst.put("drop", drop);
            ServerUtil.dropMap.remove(dropNo);
            while (ServerUtil.dropNo > ServerUtil.DROP_NO_MIN && !ServerUtil.dropMap.containsKey(String.valueOf(ServerUtil.dropNo - 1))) {
                ServerUtil.dropNo--;
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
            if (null == body) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            int sceneNo = Integer.parseInt(body.get("sceneNo").toString());
            BigDecimal x = new BigDecimal(body.get("x").toString());
            BigDecimal y = new BigDecimal(body.get("y").toString());
            String itemNo = body.get("itemNo").toString();
            int amount = Integer.parseInt(body.get("amount").toString());
            Drop drop = new Drop(sceneNo, x, y, itemNo, amount);
            ServerUtil.dropMap.put(String.valueOf(ServerUtil.dropNo), drop);
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
