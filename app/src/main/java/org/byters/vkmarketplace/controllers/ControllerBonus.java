package org.byters.vkmarketplace.controllers;

public class ControllerBonus {
    private static ControllerBonus instance;
    private int bonusCount;

    private ControllerBonus() {
        bonusCount = 0;
    }

    public static ControllerBonus getInstance() {
        if (instance == null) instance = new ControllerBonus();
        return instance;
    }

    public int getBonusCount() {
        return bonusCount;
    }
}
