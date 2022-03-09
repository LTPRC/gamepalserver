package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

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
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.model.UserStatus;
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
            if (!ServerUtil.userDataMap.containsKey(uuid)) {
                UserData userData = new UserData();
                userData.setUserCode(uuid);
                userData.setNearbySceneNos(new ArrayList<>()); // To be determined
                userData.setSceneNo(0); // To be determined
                userData.setPlayerX(new BigDecimal(2.0)); // To be determined
                userData.setPlayerY(new BigDecimal(2.0)); // To be determined
                userData.setNextSceneNo(userData.getSceneNo());
                userData.setPlayerNextX(userData.getPlayerX());
                userData.setPlayerNextY(userData.getPlayerY());
                userData.setPlayerSpeedX(new BigDecimal(0.0)); // To be determined
                userData.setPlayerSpeedY(new BigDecimal(0.0)); // To be determined
                userData.setPlayerMaxSpeedX(ServerUtil.PLAYER_SPEED_X_MAX);
                userData.setPlayerMaxSpeedY(ServerUtil.PLAYER_SPEED_Y_MAX);
                userData.setAcceleration(ServerUtil.PLAYER_ACCELERATION);
                userData.setPlayerDirection(7);
                userData.setTools(new HashSet<>());
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
                    String outfitsStr = userCharacterList.get(0).getOutfit();
                    Set<String> outfits = new HashSet<>();
                    if (StringUtils.isNotBlank(outfitsStr)) {
                        String[] outfitsStrs = userCharacterList.get(0).getOutfit().split(",");
                        for (String str : outfitsStrs) {
                            outfits.add(str);
                        }
                    }
                    userData.setOutfits(outfits);
                    userData.setAvatar(userCharacterList.get(0).getAvatar());
                }
                ServerUtil.userDataMap.put(uuid, userData);
                Set<String> userCodeSet = ServerUtil.userLocationMap.getOrDefault(userData.getSceneNo(), new ConcurrentSkipListSet<>());
                userCodeSet.add(uuid);
                ServerUtil.userLocationMap.put(userData.getSceneNo(), userCodeSet);
            }
            if (!ServerUtil.userStatusMap.containsKey(uuid)) {
                UserStatus userStatus = new UserStatus();
                userStatus.setHpMax(100); // To be determined
                userStatus.setHp(userStatus.getHpMax());
                userStatus.setVpMax(1000); // To be determined
                userStatus.setVp(userStatus.getVpMax());
                userStatus.setHungerMax(100); // To be determined
                userStatus.setHunger(userStatus.getHungerMax());
                userStatus.setThirstMax(100); // To be determined
                userStatus.setThirst(userStatus.getThirstMax());
                userStatus.setLevel(1);
                userStatus.setExp(0);
                userStatus.setExpMax(100); // To be determined
                userStatus.setMoney(0);
                userStatus.setItems(new HashMap<>());
                userStatus.setCapacityMax(new BigDecimal(50)); // To be determined
                userStatus.setCapacity(new BigDecimal(0));
                userStatus.setPreservedItems(new HashMap<>());
                userStatus.setMemberNumMax(100); // To be determined
                ServerUtil.userStatusMap.put(uuid, userStatus);
            }
            if (!ServerUtil.chatMap.containsKey(uuid)) {
                ServerUtil.chatMap.put(uuid, new ConcurrentLinkedQueue<>());
            }
            if (!ServerUtil.voiceMap.containsKey(uuid)) {
                ServerUtil.voiceMap.put(uuid, new ConcurrentLinkedQueue<>());
            }
            if (!ServerUtil.hqMap.containsKey(uuid)) {
                ServerUtil.hqMap.put(uuid, new HashMap<>());
            }
            if (!ServerUtil.relationMap.containsKey(uuid)) {
                ServerUtil.relationMap.put(uuid, new HashMap<>());
            }
            JSONObject rst = new JSONObject();
            rst.put("userCode", uuid);
            rst.put("token", ServerUtil.tokenMap.get(uuid));
            return ResponseEntity.status(HttpStatus.OK).body(JSONObject.toJSONString(rst));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    @RequestMapping(value = "/init-user-data", method = RequestMethod.POST)
    public ResponseEntity<String> initUserData(HttpServletRequest request) {
        String userCode;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            userCode = body.getString("userCode");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        UserData userData = ServerUtil.userDataMap.get(userCode);
        UserStatus userStatus = ServerUtil.userStatusMap.get(userCode);
        JSONObject rst = new JSONObject();
        rst.put("userCode", userCode);
        rst.put("token", ServerUtil.tokenMap.get(userCode));
        rst.put("userData", JSON.toJSON(userData));
        rst.put("userStatus", JSON.toJSON(userStatus));
        return ResponseEntity.status(HttpStatus.OK).body(JSONObject.toJSONString(rst));
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
            ServerUtil.userStatusMap.remove(userCode);
            ServerUtil.hqMap.remove(userCode);
        }
    }
}
