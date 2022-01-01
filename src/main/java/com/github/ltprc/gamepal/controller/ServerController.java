package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.config.map.Position;
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
                int sceneTemp = 101;
                Position positionTemp = new Position(sceneTemp, 300, 300);
                ServerUtil.positionMap.put(uuid, positionTemp);
                Set<String> uuidSet = ServerUtil.userLocationMap.containsKey(sceneTemp) 
                        ? ServerUtil.userLocationMap.get(sceneTemp) : new HashSet<>();
                uuidSet.add(uuid);
                ServerUtil.userLocationMap.put(sceneTemp, uuidSet);
            }
            String token = UUID.randomUUID().toString();
            rst.put("token", token);
            ServerUtil.tokenMap.put(uuid, token);
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
        if (ServerUtil.positionMap.containsKey(uuid)) {
            Position position = ServerUtil.positionMap.get(uuid);
            int sceneNo = position.getSceneNo();
            if (ServerUtil.userLocationMap.containsKey(sceneNo)) {
                Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
                uuidSet.remove(uuid);
            }
        }
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
            uuid = body.get("uuid").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        Position position = ServerUtil.positionMap.get(uuid);
        if (null == position) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
        }
        rst.put("x", position.getX());
        rst.put("y", position.getY());
        rst.put("sceneNo", position.getSceneNo());
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/getUsersByScene", method = RequestMethod.POST)
    public ResponseEntity<String> getUsersByScene(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        int sceneNo;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            sceneNo = (int) body.get("sceneNo");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
        if (null == uuidSet || uuidSet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
        }
        Comparator<Map.Entry<String, Position>> comparator = new Comparator<Map.Entry<String, Position>>() {
            @Override
            public int compare(Entry<String, Position> e1, Entry<String, Position> e2) {
                return e1.getValue().getY() - e2.getValue().getY();
            }
        };
        // From smaller y to bigger y
        Map<String, Position> positionMap = new TreeMap<String, Position>();
        uuidSet.stream().forEach(uuid -> positionMap.put(uuid, ServerUtil.positionMap.get(uuid)));
        rst.put("positionMap", positionMap);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
