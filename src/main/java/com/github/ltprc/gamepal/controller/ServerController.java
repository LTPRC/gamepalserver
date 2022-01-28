package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
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
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.model.map.Position;
import com.github.ltprc.gamepal.entity.UserCharacter;
import com.github.ltprc.gamepal.entity.UserInfo;
import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.repository.UserCharacterRepository;
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
    @Autowired
    UserCharacterRepository userCharacterRepository;

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
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpServletRequest request) {
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
            String token = UUID.randomUUID().toString();
            ServerUtil.tokenMap.put(uuid, token);
            ServerUtil.onlineMap.remove(uuid);
            ServerUtil.onlineMap.put(uuid, Instant.now().getEpochSecond());
            UserOnline userOnline = new UserOnline();
            userOnline.setUuid(uuid);
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            userOnline.setLoginTime(sdf.format(new Date()));
            if (userOnlineRepository.queryUserOnlineByUuid(uuid).isEmpty()) {
                userOnlineRepository.save(userOnline);
            }
            UserData userData = new UserData();
            userData.setUserCode(uuid);
            userData.setSceneNo(0); // To be determined
            userData.setNearbySceneNos(new ArrayList<>()); // To be determined
            userData.setPlayerX(new BigDecimal(3.0)); // To be determined
            userData.setPlayerY(new BigDecimal(3.0)); // To be determined
            userData.setPlayerNextX(userData.getPlayerX());
            userData.setPlayerNextY(userData.getPlayerY());
            userData.setPlayerSpeedX(new BigDecimal(0.0)); // To be determined
            userData.setPlayerSpeedY(new BigDecimal(0.0)); // To be determined
            userData.setPlayerMaxSpeedX(new BigDecimal(0.05)); // To be determined
            userData.setPlayerMaxSpeedY(new BigDecimal(0.05)); // To be determined
            userData.setAcceleration(new BigDecimal(0.01));
            userData.setPlayerDirection(7);
            List<UserCharacter> userCharacterList = userCharacterRepository.queryUserCharacterByUuid(uuid);
            if (null != userCharacterList && userCharacterList.size() > 0) {
                userData.setFirstName(userCharacterList.get(0).getFirstName());
                userData.setLastName(userCharacterList.get(0).getLastName());
                userData.setNickname(userCharacterList.get(0).getNickname());
                userData.setNameColor(userCharacterList.get(0).getNameColor());
                userData.setCreature(userCharacterList.get(0).getCreature());
                userData.setGender(userCharacterList.get(0).getGender());
                userData.setSkinColor(userCharacterList.get(0).getSkinColor());
                userData.setHairstyle(userCharacterList.get(0).getHairstyle());
                userData.setHairColor(userCharacterList.get(0).getHairColor());
                userData.setEyes(userCharacterList.get(0).getEyes());
                userData.setOutfit(userCharacterList.get(0).getOutfit());
                userData.setAvatar(userCharacterList.get(0).getAvatar());
            }
            ServerUtil.userDataMap.put(uuid, userData);
            Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(userData.getSceneNo(), new ConcurrentSkipListSet<>());
            userCodeSet.add(uuid);
            ServerUtil.userLocationMap.put(userData.getSceneNo(), userCodeSet);
            ServerUtil.chatMap.put(uuid, new ConcurrentLinkedQueue<>());
            ServerUtil.voiceMap.put(uuid, new ConcurrentLinkedQueue<>());
            return ResponseEntity.status(HttpStatus.OK).body(ServerUtil.generateReplyContent(userData));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    @RequestMapping(value = "/logoff", method = RequestMethod.POST)
    public ResponseEntity<String> logoff(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String userCode, token;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            userCode = body.getString("userCode");
            token = body.getString("token");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        afterLogoff(userCode, token);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
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
            userCodeSet.remove(userCode);
            ServerUtil.userLocationMap.put(ServerUtil.userDataMap.get(userCode).getSceneNo(), userCodeSet);
            ServerUtil.chatMap.remove(userCode);
            ServerUtil.voiceMap.remove(userCode);
            ServerUtil.userDataMap.remove(userCode);
        }
    }

    @Deprecated
    @RequestMapping(value = "/init-user-data", method = RequestMethod.POST)
    public ResponseEntity<String> initUserData(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String userCode;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            userCode = (String) body.get("userCode");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        UserData userData = ServerUtil.userDataMap.get(userCode);
        if (null == userData) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
        }
        rst.put("userData", userData);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
