package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;

import java.util.ArrayList;

public class ControllerFavorites {

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
}
