package org.byters.vkmarketplace.controllers.controllers;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketInfo;

import java.util.ArrayList;

public class ControllerMarkets {

    private ArrayList<MarketInfo> data;

    public ControllerMarkets(ControllerMain controllerMain) {
        data = (ArrayList<MarketInfo>) ControllerStorage.readObjectFromFile(controllerMain, ControllerStorage.MARKETS_CACHE);
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    public MarketInfo getItem(int position) {
        if (data == null) return null;
        if (position < 0 || position >= data.size()) return null;
        return data.get(position);
    }
}
