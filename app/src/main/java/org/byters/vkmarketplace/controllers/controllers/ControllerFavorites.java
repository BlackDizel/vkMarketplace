package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.dataclasses.FavoriteItem;

import java.util.ArrayList;

public class ControllerFavorites {

    public static final int NO_VALUE = -1;
    private ArrayList<FavoriteItem> data;

    public ControllerFavorites(Context context) {
        data = (ArrayList<FavoriteItem>) ControllerStorage.readObjectFromFile(context, ControllerStorage.FAVORITES_CACHE);
    }

    public void addItem(Context context, int itemId) {
        if (data == null) data = new ArrayList<>();
        FavoriteItem item = new FavoriteItem();
        item.setId(itemId);
        item.setTime_added(System.currentTimeMillis());
        data.add(item);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.FAVORITES_CACHE);
    }

    public boolean isFavorite(int id) {
        return getItemPosition(id) != NO_VALUE;
    }

    public void toggleFavorite(Context context, int id) {
        if (isFavorite(id)) {
            removeItemById(id);
            ControllerStorage.writeObjectToFile(context, data, ControllerStorage.FAVORITES_CACHE);
        } else addItem(context, id);
    }

    private void removeItemById(int id) {
        if (data == null) return;
        int pos = getItemPosition(id);
        if (pos == NO_VALUE) return;
        data.remove(pos);
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    public FavoriteItem getItem(int position) {
        if (data == null || position < 0 || position >= data.size()) return null;
        return data.get(position);
    }

    public int getItemPosition(int id) {
        if (data == null) return NO_VALUE;
        for (FavoriteItem item : data)
            if (item.getId() == id)
                return data.indexOf(item);
        return NO_VALUE;
    }

    public void removeItem(int pos) {
        if (data == null) return;
        data.remove(pos);
    }
}
