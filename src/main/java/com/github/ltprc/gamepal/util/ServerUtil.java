package com.github.ltprc.gamepal.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class ServerUtil {

    public final static String API_PATH = "/api/v1";

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
