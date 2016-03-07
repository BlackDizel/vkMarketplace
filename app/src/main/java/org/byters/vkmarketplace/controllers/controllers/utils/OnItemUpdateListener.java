package org.byters.vkmarketplace.controllers.controllers.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public interface OnItemUpdateListener {
    void onItemLoaded(@NonNull MarketplaceItem item);

    void onItemLoadError(int itemId);
}
