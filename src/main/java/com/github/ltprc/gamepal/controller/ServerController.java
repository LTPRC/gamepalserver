package com.github.ltprc.gamepal.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserInfo;
import com.github.ltprc.gamepal.repository.UserInfoRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping("/v1")
public class ServerController {

    @Autowired
    UserInfoRepository userInfoRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject register(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        UserInfo userInfo = new UserInfo();
        String uuid = UUID.randomUUID().toString();
        while (!userInfoRepository.queryUserInfoByUuid(uuid).isEmpty()) {
            uuid = UUID.randomUUID().toString();
        }
        String username, password;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            username = body.get("username").toString();
            userInfo.setUsername(username);
            password = body.get("password").toString();
            userInfo.setPassword(password);
        } catch (IOException e) {
            rst.put("status", ServerUtil.RESPOND_CODE_FAILED);
            return rst;
        }
        if (!userInfoRepository.queryUserInfoByUsername(username).isEmpty()) {
            rst.put("status", ServerUtil.RESPOND_CODE_FAILED);
            return rst;
        }
        userInfo.setUuid(uuid.toString());
        userInfo.setStatus(1);
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        userInfo.setCreateTime(sdf.format(new Date()));
        userInfo.setUpdateTime(userInfo.getCreateTime());
        userInfoRepository.save(userInfo);
        rst.put("status", ServerUtil.RESPOND_CODE_SUCCESS);
        return rst;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String username, password;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            username = body.get("username").toString();
            password = body.get("password").toString();
        } catch (IOException e) {
            rst.put("status", ServerUtil.RESPOND_CODE_FAILED);
            return rst;
        }
        if (userInfoRepository.queryUserInfoByUsernameAndPassword(username, password).isEmpty()) {
            rst.put("status", ServerUtil.RESPOND_CODE_FAILED);
            return rst;
        }
        // userOnlineRepository.save(username?);
        rst.put("status", ServerUtil.RESPOND_CODE_SUCCESS);
        return rst;
    }

//    @RequestMapping("/query-user")
//    @ResponseBody
//    public String queryUser(HttpServletRequest request) {
//        
//    }
    
}
