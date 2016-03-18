package org.byters.vkmarketplace.controllers.controllers;

import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ControllerSuggestions {
    public static final int NO_VALUE = -1;

    private ControllerMain controllerMain;
    private int savedId;
    private ArrayList<Integer> data;
    private Random random;

    public ControllerSuggestions(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
        this.random = new Random();
    }

    public int getSize(int itemId) {
        if (savedId != itemId) {
            savedId = itemId;
            generateItems();
        }

        return data.size();
    }

    private void generateItems() {
        data = new ArrayList<>();
        int maxSize = controllerMain.getControllerItems().getModel().getSize();
        if (maxSize <= 1) return; //if one item in market, no suggestion available

        ArrayList<Integer> shuffledIds = new ArrayList<>();

        for (int i = 0; i < maxSize; ++i) {
            MarketplaceItem item = controllerMain.getControllerItems().getModel().get(i);
            if (item == null || item.getId() == savedId)
                continue;
            shuffledIds.add(item.getId());
        }
        Collections.shuffle(shuffledIds);

        int realSize = Math.min(4 + random.nextInt(3), maxSize - 1);
        for (int i = 0; i < realSize; ++i)
            data.add(shuffledIds.get(i));
    }

    public int getItem(int itemId, int position) {
        if (savedId != itemId)
            generateItems();
        if (position < 0 || position >= data.size())
            return NO_VALUE;
        return data.get(position);
    }

}
