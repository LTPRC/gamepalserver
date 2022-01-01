package com.github.ltprc.gamepal.config.map;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    // Relocate to Frontend!
    public static final List<Scene> scenes = null;
    public static final List<String> floorMenu;
    public static final List<String> wallMenu;
    public static final List<String> decorationMenu;
    public static final List<String> characterMenu;
    public static final String FLOOR_WOODEN_0 = "f_0_0"; // 交错木地板
    public static final String FLOOR_WOODEN_1 = "f_0_1"; // 横木地板

    static {
        floorMenu = new ArrayList<>(100);
        
        wallMenu = new ArrayList<>(100);
        
        decorationMenu = new ArrayList<>(100);
        
        characterMenu = new ArrayList<>(100);
    }
}
