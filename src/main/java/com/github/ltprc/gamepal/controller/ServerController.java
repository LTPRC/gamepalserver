package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
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
            UserOnline userOnline = new UserOnline();
            userOnline.setUuid(uuid);
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
            userOnline.setLoginTime(sdf.format(new Date()));
            userOnlineRepository.save(userOnline);
            return ResponseEntity.status(HttpStatus.OK).body("Login succeeded");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

//    @RequestMapping("/query-user")
//    @ResponseBody
//    public String queryUser(HttpServletRequest request) {
//        
//    }
    
}
