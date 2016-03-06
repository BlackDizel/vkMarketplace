package org.byters.vkmarketplace.model.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.controllers.controllers.ControllerStorage;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public class ModelItems {

    private ArrayList<MarketplaceItem> data;

    public ModelItems(Context context) {
        data = (ArrayList<MarketplaceItem>) ControllerStorage.readObjectFromFile(context, ControllerStorage.ITEMS_INFO);
    }

    public boolean isNull() {
        return data == null;
    }

    public void setItem(@NonNull Context context, @NonNull MarketplaceItem newItem) {
        MarketplaceItem item = getItemById(newItem.getId());
        if (item == null) return;
        int index = data.indexOf(item);
        data.set(index, newItem);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.ITEMS_INFO);
    }

    public void setData(Context context, ArrayList<MarketplaceItem> data, boolean isCacheNeeded) {
        if (data == null || data.size() == 0) {
            this.data = null;
        } else {
            this.data = data;
        }
        if (isCacheNeeded)
            ControllerStorage.writeObjectToFile(context, data, ControllerStorage.ITEMS_INFO);
    }

    public void clearData(Context context) {
        data = null;
        ControllerStorage.RemoveFile(context, ControllerStorage.ITEMS_INFO);
    }

    public int getSize() {
        if (data == null)
            return 0;
        return data.size();
    }

    public MarketplaceItem get(int position) {
        if (position >= 0 && position < getSize())
            return data.get(position);
        return null;
    }

    @Nullable
    public MarketplaceItem getItemById(int id) {
        if (data == null) return null;
        for (MarketplaceItem item : data)
            if (item.getId() == id) return item;
        return null;
    }
}
