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
import com.github.ltprc.gamepal.config.ServerConstant;
import com.github.ltprc.gamepal.entity.UserInfo;
import com.github.ltprc.gamepal.repository.UserInfoRepository;

@RestController
@RequestMapping("/v1")
public class ServerController {

    @Autowired
    UserInfoRepository userInfoRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        UserInfo userInfo = new UserInfo();
        String uuid = UUID.randomUUID().toString();
        while (!userInfoRepository.queryUserInfoByUuid(uuid).isEmpty()) {
            uuid = UUID.randomUUID().toString();
        }
        String username, password;
        try {
            BufferedReader br = request.getReader();
            String str = "";
            String listString = "";
            while ((str = br.readLine()) != null) {
                listString += str;
            }
            JSONObject jsonObject = (JSONObject) JSONObject.parse(listString);
            username = jsonObject.get("username").toString();
            userInfo.setUsername(username);
            password = jsonObject.get("password").toString();
            userInfo.setPassword(password);
        } catch (IOException e) {
            rst.put("status", ServerConstant.RESPOND_CODE_FAILED);
            return rst.toString();
        }
        if (!userInfoRepository.queryUserInfoByUsername(username).isEmpty()) {
            rst.put("status", ServerConstant.RESPOND_CODE_FAILED);
            return rst.toString();
        }
        userInfo.setUuid(uuid.toString());
        userInfo.setStatus(1);
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        userInfo.setCreateTime(sdf.format(new Date()));
        userInfo.setUpdateTime(userInfo.getCreateTime());
        userInfoRepository.save(userInfo);
        rst.put("status", ServerConstant.RESPOND_CODE_SUCCESS);
        return rst.toString();
    }

//    @RequestMapping("/query-user")
//    @ResponseBody
//    public String queryUser(HttpServletRequest request) {
//        
//    }
}
