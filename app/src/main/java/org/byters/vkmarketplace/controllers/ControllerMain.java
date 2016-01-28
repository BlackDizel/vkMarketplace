package org.byters.vkmarketplace.controllers;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;

public class ControllerMain extends Application {

    private ControllerAuth controllerAuth;

    @Override
    public void onCreate() {
        super.onCreate();

        //todo set imageloader config
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        controllerAuth = new ControllerAuth(this);
    }

    public boolean isAuth() {
        return controllerAuth.isAuth();
    }

    public void setToken(@NonNull String key) {
        controllerAuth.setToken(this, key);
    }
}
