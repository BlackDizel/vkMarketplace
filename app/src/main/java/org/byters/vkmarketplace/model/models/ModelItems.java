package org.byters.vkmarketplace.model.models;

import android.content.Context;

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


    public void setData(Context context, ArrayList<MarketplaceItem> data) {
        if (data == null || data.size() == 0) {
            this.data = null;
        } else {
            this.data = data;
        }
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
}
