package org.byters.vkmarketplace.controllers;

import android.app.Application;
import android.support.annotation.NonNull;

import org.byters.vkmarketplace.controllers.controllers.ControllerAlbums;
import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;
import org.byters.vkmarketplace.controllers.controllers.ControllerCart;
import org.byters.vkmarketplace.controllers.controllers.ControllerFavorites;
import org.byters.vkmarketplace.controllers.controllers.ControllerItemInfo;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.controllers.ControllerSearchResult;
import org.byters.vkmarketplace.controllers.controllers.ControllerUserData;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class ControllerMain extends Application
        implements OnItemUpdateListener {

    private ControllerAuth controllerAuth;
    private ControllerItems controllerItems;
    private ControllerItemInfo controllerItemInfo;
    private ControllerCart controllerCart;
    private ControllerFavorites controllerFavorites;
    private ControllerNews controllerNews;
    private ControllerSearchResult controllerSearchResult;
    private ControllerUserData controllerUserData;
    private ControllerAlbums controllerAlbums;

    public ControllerItems getControllerItems() {
        return controllerItems;
    }

    public ControllerItemInfo getControllerItemInfo() {
        return controllerItemInfo;
    }

    public ControllerCart getControllerCart() {
        return controllerCart;
    }

    public ControllerFavorites getControllerFavorites() {
        return controllerFavorites;
    }

    public ControllerNews getControllerNews() {
        return controllerNews;
    }

    public ControllerSearchResult getControllerSearchResult() {
        if (controllerSearchResult == null)
            controllerSearchResult = new ControllerSearchResult(this);
        return controllerSearchResult;
    }

    public ControllerUserData getControllerUserData() {
        if (controllerUserData == null)
            controllerUserData = new ControllerUserData(this);
        return controllerUserData;
    }

    public ControllerAlbums getControllerAlbums() {
        if (controllerAlbums == null)
            controllerAlbums = new ControllerAlbums(this);
        return controllerAlbums;
    }

    public void updateAlbums() {
        getControllerAlbums().updateData(controllerAuth.getToken());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //todo set imageloader config
        controllerAuth = new ControllerAuth(this);
        controllerItems = new ControllerItems(this, controllerAuth.getToken());
        controllerItemInfo = new ControllerItemInfo();
        controllerItemInfo.addItemInfoUpdatedListener(this);
        controllerCart = new ControllerCart(this);
        controllerFavorites = new ControllerFavorites(this);
        controllerNews = new ControllerNews(this);
    }

    public boolean isAuth() {
        return controllerAuth.isAuth();
    }

    public void setToken(@NonNull String key) {
        controllerAuth.setToken(this, key);
        controllerItems.updateData(this, key);
        controllerNews.updateData(this);
    }

    public void setUserID(@NonNull String id) {
        controllerAuth.setUserId(this, id);
    }

    public void updateMarketList() {
        controllerItems.updateData(this, controllerAuth.getToken());
    }

    public void updateNews() {
        controllerNews.updateData(this);
    }

    public void updateUserData() {
        getControllerUserData().updateUserData(controllerAuth.getUserId());
    }

    public void searchItems(String query) {
        getControllerSearchResult().search(query, controllerAuth.getToken());
    }

    public void updateDetailedItemInfo(int itemId) {
        MarketplaceItem item = controllerItems.getModel().getItemById(itemId);
        if (item != null && item.getPhotos() != null) {
            controllerItemInfo.updateListeners(item);
            return;
        }
        controllerItemInfo.getItemInfo(this, itemId, controllerAuth.getToken());
    }

    @Override
    public void onItemLoaded(@NonNull MarketplaceItem item) {
        controllerItems.getModel().setItem(this, item);
    }
}
