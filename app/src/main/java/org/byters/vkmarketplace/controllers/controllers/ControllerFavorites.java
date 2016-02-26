package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;

import java.util.ArrayList;

public class ControllerFavorites {

    public static final int NO_VALUE = -1;
    private ArrayList<Integer> data;

    public ControllerFavorites(Context context) {
        data = (ArrayList<Integer>) ControllerStorage.readObjectFromFile(context, ControllerStorage.FAVORITES_CACHE);
    }

    public void addItem(Context context, int itemId) {
        if (data == null) data = new ArrayList<>();
        data.add(itemId);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.FAVORITES_CACHE);
    }

    public boolean isFavorite(int id) {
        //todo check
        if (data == null) return false;
        return data.contains(id);
    }

    public void toggleFavorite(Context context, int id) {
        if (isFavorite(id)) {
            data.remove((Integer) id);
            ControllerStorage.writeObjectToFile(context, data, ControllerStorage.FAVORITES_CACHE);
        } else addItem(context, id);
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public int getItem(int position) {
        if (data == null || position < 0 || position >= data.size()) return NO_VALUE;
        return data.get(position);
    }

    public int getItemPosition(int id) {
        if (data == null) return NO_VALUE;
        for (int item : data)
            if (item == id)
                return data.indexOf(item);
        return NO_VALUE;
    }

    public void removeItem(int pos) {
        if (data == null) return;
        data.remove(pos);
    }
}
