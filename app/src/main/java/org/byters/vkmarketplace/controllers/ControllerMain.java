package org.byters.vkmarketplace.controllers;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;

public class ControllerMain extends Application {

    private ControllerAuth controllerAuth;
    private ControllerItems controllerItems;

    public ControllerItems getControllerItems() {
        return controllerItems;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //todo set imageloader config
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        controllerAuth = new ControllerAuth(this);
        controllerItems = new ControllerItems(this, controllerAuth.getToken());
    }

    public boolean isAuth() {
        return controllerAuth.isAuth();
    }

    public void setToken(@NonNull String key) {
        controllerAuth.setToken(this, key);
        controllerItems.updateData(this, key);
    }


    public void updateMarketList() {
        controllerItems.updateData(this, controllerAuth.getToken());
    }
}
