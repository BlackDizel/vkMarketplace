package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;

import org.byters.vkmarketplace.model.ModelAuth;

public class ControllerAuth {

    private ModelAuth model;

    public ControllerAuth(Context context) {
        model = new ModelAuth(context);
    }

    public boolean isAuth() {
        return model.isAuth();
    }

    public void setToken(Context context, String key) {
        model.setData(context,key);
    }
}