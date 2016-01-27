package org.byters.vkmarketplace.controllers.controllers.utils;

import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public interface ItemsUpdateListener {
    void onUpdated(ArrayList<MarketplaceItem> data);
}
