package org.byters.vkmarketplace.model;

import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public class MarketplaceBlob {

    private ArrayList<MarketplaceItem> response;

    public ArrayList<MarketplaceItem> getItems() {
        //todo check
        if (response == null || response.size() == 0) return null;

        ArrayList<MarketplaceItem> items = new ArrayList<>();
        for (MarketplaceItem item : response)
            if (item != null) items.add(item);

        if (items.size() == 0) return null;
        return items;
    }
}
