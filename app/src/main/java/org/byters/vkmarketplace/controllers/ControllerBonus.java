package org.byters.vkmarketplace.controllers;

import org.byters.vkmarketplace.BuildConfig;

public class ControllerBonus {
    private static ControllerBonus instance;
    private int bonusCount;

    private ControllerBonus() {
        if (BuildConfig.DEBUG)
            bonusCount = 500;
    }

    public static ControllerBonus getInstance() {
        if (instance == null) instance = new ControllerBonus();
        return instance;
    }

    public int getBonusCount() {
        return bonusCount;
    }
}
