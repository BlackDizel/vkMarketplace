package org.byters.vkmarketplace.controllers.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.dataclasses.OrderHistoryInfo;

import java.util.ArrayList;
import java.util.Collections;

public class ControllerOrdersHistory {

    private static ControllerOrdersHistory instance;
    private ArrayList<OrderHistoryInfo> data;

    private ControllerOrdersHistory() {
        data = (ArrayList<OrderHistoryInfo>) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.ORDERS_HISTORY_CACHE);
    }

    public static ControllerOrdersHistory getInstance() {
        if (instance == null) instance = new ControllerOrdersHistory();
        return instance;
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

    public void addItem(@NonNull OrderHistoryInfo info) {
        if (data == null) data = new ArrayList<>();
        data.add(info);
        Collections.sort(data);
        Collections.reverse(data);
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.ORDERS_HISTORY_CACHE);
    }

    public void clearData() {
        data = null;
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.ORDERS_HISTORY_CACHE);
    }

}
