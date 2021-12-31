package com.github.ltprc.gamepal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.config.map.Position;

public class ServerUtil {

    public final static String API_PATH = "/api/v1";

    public final static Map<String, Position> positionMap = new HashMap<>(); // uuid, position
    public final static Map<Integer, Set<String>> userLocationMap = new HashMap<>(); // sceneNo, uuid

    public static JSONObject strRequest2JSONObject(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str = "";
        String listString = "";
        while ((str = br.readLine()) != null) {
            listString += str;
        }
       return (JSONObject) JSONObject.parse(listString);
    }
}
