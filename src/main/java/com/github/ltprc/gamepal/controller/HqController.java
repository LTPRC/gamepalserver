package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.model.MemberData;
import com.github.ltprc.gamepal.util.NameUtil;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class HqController {

    @RequestMapping(value = "/get-members", method = RequestMethod.POST)
    public ResponseEntity<String> getMembers(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            Map<String, MemberData> memberMap = ServerUtil.hqMap.getOrDefault(userCode, new HashMap<>());
            rst.put("members", memberMap);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/insert-member", method = RequestMethod.POST)
    public ResponseEntity<String> insertMember(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            Map<String, MemberData> memberMap = ServerUtil.hqMap.get(userCode);
            if (memberMap.size() >= ServerUtil.userStatusMap.get(userCode).getMemberNumMax()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member map is full");
            }
            MemberData memberData = new MemberData();
            String memberCode = UUID.randomUUID().toString();
            memberData.setMemberCode(memberCode);
            memberData.setUserCode(userCode);
            double r = Math.random();
            String gender = r < 0.5D ? NameUtil.GENDER_MALE : NameUtil.GENDER_FEMALE;
            String[] names = NameUtil.generateNames("", gender);
            memberData.setFirstName(names[0]);
            memberData.setLastName(names[1]);
            memberData.setNickname(names[2]);
            memberData.setCreature("1");
            memberData.setGender(gender);
            r = Math.random();
            String skinColor;
            if (r < 0.5D) {
                skinColor = "1";
            } else if (r < 0.7D) {
                skinColor = "2";
            } else if (r < 0.9D) {
                skinColor = "3";
            } else {
                skinColor = "4";
            }
            memberData.setSkinColor(skinColor);
            Random random = new Random();
            String hairstyle = NameUtil.GENDER_MALE.equals(gender) ? String.valueOf(random.nextInt(7) + 1) : String.valueOf(random.nextInt(6) + 7);
            memberData.setHairstyle(hairstyle);
            r = Math.random();
            String hairColor;
            if (r < 0.8D) {
                hairColor = "1";
            } else if (r < 0.9D) {
                hairColor = "2";
            } else {
                hairColor = "3";
            }
            memberData.setHairColor(hairColor);
            String eyes = String.valueOf(random.nextInt(12) + 1);
            memberData.setEyes(eyes);
            memberData.setTools(new HashSet<>());
            memberData.setOutfits(new HashSet<>());
            memberData.setAvatar(1);
            memberData.setHpMax(100); // To be determined
            memberData.setHp(memberData.getHpMax());
            memberData.setVpMax(1000); // To be determined
            memberData.setVp(memberData.getVpMax());
            memberData.setHungerMax(100); // To be determined
            memberData.setHunger(memberData.getHungerMax());
            memberData.setThirstMax(100); // To be determined
            memberData.setThirst(memberData.getThirstMax());
            memberData.setLevel(1);
            memberData.setExp(0);
            memberData.setExpMax(100); // To be determined
            ServerUtil.hqMap.get(userCode).put(memberCode, memberData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/delete-member", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMember(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            Map<String, MemberData> memberMap = ServerUtil.hqMap.get(userCode);
            String memberCode = body.get("memberCode").toString();
            if (!memberMap.containsKey(memberCode)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }
            ServerUtil.hqMap.get(userCode).remove(memberCode);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @Deprecated
    @RequestMapping(value = "/insert-pal", method = RequestMethod.POST)
    public ResponseEntity<String> insertPal(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            if (ServerUtil.userStatusMap.get(userCode).getPalMap().size() >= ServerUtil.userStatusMap.get(userCode).getPalNumMax()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pal list is full");
            }
            String palCode = body.get("palCode").toString();
            if (!ServerUtil.hqMap.containsKey(userCode)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hq information found");
            }
            if (!ServerUtil.hqMap.get(userCode).containsKey(palCode)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pal is not found from member map");
            }
            MemberData memberData = ServerUtil.hqMap.get(userCode).get(palCode);
//            ServerUtil.userDataMap.put(palCode, memberData);
            // To be continued
            rst.put(palCode, JSON.toJSON(memberData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @Deprecated
    @RequestMapping(value = "/delete-pal", method = RequestMethod.POST)
    public ResponseEntity<String> deletePal(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            String userCode = body.get("userCode").toString();
            if (ServerUtil.userStatusMap.get(userCode).getPalMap().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pal list is empty");
            }
            String palCode = body.get("palCode").toString();
            /**
             * Remove all possible references
             */
            ServerUtil.userDataMap.remove(palCode);
//            UserStatus userStatus = ServerUtil.userStatusMap.get(userCode);
//            userStatus.getPalMap().remove(palCode);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
