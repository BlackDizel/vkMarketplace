package org.byters.vkmarketplace.controllers;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ControllerMain extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //todo set imageloader config
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }
}
