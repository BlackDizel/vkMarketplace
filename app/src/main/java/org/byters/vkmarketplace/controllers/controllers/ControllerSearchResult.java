package org.byters.vkmarketplace.controllers.controllers;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public class ControllerSearchResult {

    private ArrayList<MarketplaceItem> data;
    private ControllerMain controllerMain;

    public ControllerSearchResult(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    public int getSize() {
        if (data == null)
            return controllerMain.getControllerItems().getModel().getSize();
        return data.size();
    }

    @Nullable
    public MarketplaceItem getItem(int position) {
        if (data == null)
            return controllerMain.getControllerItems().getModel().get(position);
        if (position >= 0 && position < getSize())
            return data.get(position);
        return null;
    }
}
