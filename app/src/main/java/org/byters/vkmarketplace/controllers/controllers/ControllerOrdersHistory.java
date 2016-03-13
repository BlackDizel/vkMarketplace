package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.OrderHistoryInfo;

import java.util.ArrayList;

public class ControllerOrdersHistory {

    private ArrayList<OrderHistoryInfo> data;

    public ControllerOrdersHistory(ControllerMain controllerMain) {
        data = (ArrayList<OrderHistoryInfo>) ControllerStorage.readObjectFromFile(controllerMain, ControllerStorage.ORDERS_HISTORY_CACHE);
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    public OrderHistoryInfo getItem(int position) {
        if (data == null) return null;
        if (position < 0 || position >= data.size())
            return null;
        return data.get(position);
    }

    public void addItem(Context context, @NonNull OrderHistoryInfo info) {
        if (data == null) data = new ArrayList<>();
        data.add(info);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.ORDERS_HISTORY_CACHE);
    }

}
