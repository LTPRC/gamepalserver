package com.github.ltprc.gamepal.config.map;

import java.util.Map;

public class Scene {

    private String sceneName;
    private Map<Position, String> map;
    public String getSceneName() {
        return sceneName;
    }
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
    public Map<Position, String> getMap() {
        return map;
    }
    public void setMap(Map<Position, String> map) {
        this.map = map;
    }
}
