package org.byters.vkmarketplace.controllers.controllers;

import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ControllerSuggestions {
    public static final int NO_VALUE = -1;
    private static ControllerSuggestions instance;
    private int savedId;
    private ArrayList<Integer> data;
    private Random random;

    private ControllerSuggestions() {
        this.random = new Random();
    }

    public static ControllerSuggestions getInstance() {
        if (instance == null) instance = new ControllerSuggestions();
        return instance;
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
        int maxSize = ControllerItems.getInstance().getModel().getSize();
        if (maxSize <= 1) return; //if one item in market, no suggestion available

        ArrayList<Integer> shuffledIds = new ArrayList<>();

        for (int i = 0; i < maxSize; ++i) {
            MarketplaceItem item = ControllerItems.getInstance().getModel().get(i);
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
