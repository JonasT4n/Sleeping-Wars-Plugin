package com.joenastan.sleepingwar.plugin.Game;

import java.util.ArrayList;
import java.util.List;

public enum ResourcesType {
    IRON, 
    GOLD, 
    DIAMOND, 
    EMERALD;

    public static ResourcesType getType(String name) {
        if (name.equalsIgnoreCase("iron")) 
            return IRON;
        else if (name.equalsIgnoreCase("gold"))
            return GOLD;
        else if (name.equalsIgnoreCase("diamond"))
            return DIAMOND;
        else if (name.equalsIgnoreCase("emerald"))
            return EMERALD;
        else 
            return null;
    }

    public static List<String> getAcceptedStrings() {
        List<String> names = new ArrayList<String>();
        names.add("iron");
        names.add("gold");
        names.add("diamond");
        names.add("emerald");
        return names;
    }
}