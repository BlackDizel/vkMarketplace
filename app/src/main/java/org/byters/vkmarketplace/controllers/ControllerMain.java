package org.byters.vkmarketplace.controllers;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;
import org.byters.vkmarketplace.controllers.controllers.ControllerItemInfo;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class ControllerMain extends Application implements OnItemUpdateListener {

    private ControllerAuth controllerAuth;
    private ControllerItems controllerItems;
    private ControllerItemInfo controllerItemInfo;

    public ControllerItems getControllerItems() {
        return controllerItems;
    }

    public ControllerItemInfo getControllerItemInfo() {
        return controllerItemInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //todo set imageloader config
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        controllerAuth = new ControllerAuth(this);
        controllerItems = new ControllerItems(this, controllerAuth.getToken());
        controllerItemInfo = new ControllerItemInfo();
        controllerItemInfo.addItemInfoUpdatedListener(this);
    }

    public boolean isAuth() {
        return controllerAuth.isAuth();
    }

    public void setToken(@NonNull String key) {
        controllerAuth.setToken(this, key);
        controllerItems.updateData(this, key);

        //todo update detailed item info
    }

    public void updateMarketList() {
        controllerItems.updateData(this, controllerAuth.getToken());
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
