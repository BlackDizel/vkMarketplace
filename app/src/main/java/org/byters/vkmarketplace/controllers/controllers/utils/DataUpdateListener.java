package org.byters.vkmarketplace.controllers.controllers.utils;

public interface DataUpdateListener {
    int TYPE_ITEMS = 0;
    int TYPE_NEWS = 1;
    int TYPE_SEARCH = 2;
    int TYPE_ALBUM = 3;

    void onUpdated(int type);
}
