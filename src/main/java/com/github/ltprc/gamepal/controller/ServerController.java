package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.map.Position;
import com.github.ltprc.gamepal.model.map.UserPosition;
import com.github.ltprc.gamepal.entity.UserInfo;
import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.repository.UserInfoRepository;
import com.github.ltprc.gamepal.repository.UserOnlineRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class ServerController {

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    UserOnlineRepository userOnlineRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        UserInfo userInfo = new UserInfo();
        String uuid = UUID.randomUUID().toString();
        while (!userInfoRepository.queryUserInfoByUuid(uuid).isEmpty()) {
            uuid = UUID.randomUUID().toString();
        }
        String username, password;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            username = body.get("username").toString();
            userInfo.setUsername(username);
            password = body.get("password").toString();
            userInfo.setPassword(password);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
        if (!userInfoRepository.queryUserInfoByUsername(username).isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
        userInfo.setUuid(uuid.toString());
        userInfo.setStatus(1);
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        userInfo.setCreateTime(sdf.format(new Date()));
        userInfo.setUpdateTime(userInfo.getCreateTime());
        userInfoRepository.save(userInfo);
        return ResponseEntity.status(HttpStatus.OK).body("Registration succeeded");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String username, password;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            username = body.get("username").toString();
            password = body.get("password").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
        List<UserInfo> userInfoList = userInfoRepository.queryUserInfoByUsernameAndPassword(username, password);
        if (!userInfoList.isEmpty()) {
            String uuid = (String) userInfoList.get(0).getUuid();
            rst.put("uuid", uuid);
            if (!ServerUtil.positionMap.containsKey(uuid)) {
                // Initialize position
                int sceneTemp = 0;
                Position positionTemp = new Position(sceneTemp, new BigDecimal(-1), new BigDecimal(-1), 0,
                        new BigDecimal(0), new BigDecimal(0), 7);
                ServerUtil.positionMap.put(uuid, positionTemp);
                Set<String> uuidSet = ServerUtil.userLocationMap.containsKey(sceneTemp)
                        ? ServerUtil.userLocationMap.get(sceneTemp)
                        : new ConcurrentSkipListSet<>();
                uuidSet.add(uuid);
                ServerUtil.userLocationMap.put(sceneTemp, uuidSet);
            }
            String token = UUID.randomUUID().toString();
            rst.put("token", token);
            ServerUtil.tokenMap.put(uuid, token);
            if (!ServerUtil.chatMap.containsKey(uuid)) {
                ServerUtil.chatMap.put(uuid, new ConcurrentLinkedQueue<>());
            }
            UserOnline userOnline = new UserOnline();
            userOnline.setUuid(uuid);
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            userOnline.setLoginTime(sdf.format(new Date()));
            if (userOnlineRepository.queryUserOnlineByUuid(uuid).isEmpty()) {
                userOnlineRepository.save(userOnline);
            }
            return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    @RequestMapping(value = "/logoff", method = RequestMethod.POST)
    public ResponseEntity<String> logoff(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid, token;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = body.getString("uuid");
            token = body.getString("token");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        List<UserOnline> userOnlineList = userOnlineRepository.queryUserOnlineByUuid(uuid);
        if (!userOnlineList.isEmpty()) {
            userOnlineRepository.delete(userOnlineList.get(0));
        }
//        if (ServerUtil.positionMap.containsKey(uuid)) {
//            Position position = ServerUtil.positionMap.get(uuid);
//            int sceneNo = position.getSceneNo();
//            if (ServerUtil.userLocationMap.containsKey(sceneNo)) {
//                Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
//                uuidSet.remove(uuid);
//            }
//        }
        ServerUtil.chatMap.remove(uuid, new ConcurrentLinkedQueue<>());
        if (token.equals(ServerUtil.tokenMap.get(uuid))) {
            ServerUtil.tokenMap.remove(uuid);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    public ResponseEntity<String> checkToken(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid, token;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = body.getString("uuid");
            token = body.getString("token");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        // If there is no token found, old token will be automatically filled
        if (!ServerUtil.tokenMap.containsKey(uuid)) {
            ServerUtil.tokenMap.put(uuid, token);
        }
        if (!token.equals(ServerUtil.tokenMap.get(uuid))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/getPosition", method = RequestMethod.POST)
    public ResponseEntity<String> getPosition(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = (String) body.get("uuid");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        Position position = ServerUtil.positionMap.get(uuid);
        if (null == position) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
        }
        rst.put("position", position);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/setPosition", method = RequestMethod.POST)
    public ResponseEntity<String> setPosition(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            if (null == body || null == body.get("sceneNo") || null == body.get("uuid") || null == body.get("sceneNo")
                    || null == body.get("x") || null == body.get("y") || null == body.get("outfit")
                    || null == body.get("speedX") || null == body.get("speedY") || null == body.get("direction")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            uuid = (String) body.get("uuid");
            /**
             * Remove old information
             */
            if (ServerUtil.positionMap.containsKey(uuid)) {
                int formerSceneNo = ServerUtil.positionMap.get(uuid).getSceneNo();
                if (ServerUtil.userLocationMap.containsKey(formerSceneNo)) {
                    Set<String> formerUuidSet = ServerUtil.userLocationMap.get(formerSceneNo);
                    formerUuidSet.remove(uuid);
                }
            }
            Position position = new Position();
            position.setSceneNo((int) body.get("sceneNo"));
            position.setX(new BigDecimal(body.get("x").toString()));
            position.setY(new BigDecimal(body.get("y").toString()));
            position.setOutfit((int) body.get("outfit"));
            position.setSpeedX(new BigDecimal(body.get("speedX").toString()));
            position.setSpeedY(new BigDecimal(body.get("speedY").toString()));
            position.setDirection((int) body.get("direction"));
            ServerUtil.positionMap.put(uuid, position);
            if (ServerUtil.userLocationMap.containsKey(position.getSceneNo())) {
                Set<String> uuidSet = ServerUtil.userLocationMap.get(position.getSceneNo());
                uuidSet.add(uuid);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/getUsersByScene", method = RequestMethod.POST)
    public ResponseEntity<String> getUsersByScene(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid;
        int sceneNo;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            if (null == body || null == body.get("sceneNo") || null == body.get("uuid")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            sceneNo = (int) body.get("sceneNo");
            uuid = (String) body.get("uuid");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        // From smaller y to bigger y
        Comparator<UserPosition> comparator = new Comparator<UserPosition>() {
            @Override
            public int compare(UserPosition up1, UserPosition up2) {
                return up1.getY().compareTo(up2.getY());
            }
        };
        Queue<UserPosition> queue = new PriorityQueue<>(comparator);
        Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
        if (null != uuidSet && !uuidSet.isEmpty()) {
            // 不包括自己 不然永远别想动了！
            Iterator<String> uuidSetIterator = uuidSet.iterator();
            while (uuidSetIterator.hasNext()) {
                String otherUuid = uuidSetIterator.next();
                if (!otherUuid.equals(uuid)) {
                    queue.add(new UserPosition(otherUuid, ServerUtil.positionMap.get(otherUuid)));
                }
            }
        }
        rst.put("positionMap", queue);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
