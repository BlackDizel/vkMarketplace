package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class MarketplaceBlob {

    private MarketplaceItemBlob response;

    @Nullable
    public ArrayList<MarketplaceItem> getItems() {
        if (response == null) return null;
        return response.getItems();
    }

    private class MarketplaceItemBlob {

        private ArrayList<MarketplaceItem> items;

        @Nullable
        public ArrayList<MarketplaceItem> getItems() {
            if (items == null || items.size() == 0) return null;
            return items;
        }
    }
}
