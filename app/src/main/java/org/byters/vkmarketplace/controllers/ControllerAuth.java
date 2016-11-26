package org.byters.vkmarketplace.controllers;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.models.ModelAuth;

public class ControllerAuth {

    private static ControllerAuth instance;
    private ModelAuth model;

    private ControllerAuth() {
        model = new ModelAuth();
    }

    public static ControllerAuth getInstance() {
        if (instance == null) instance = new ControllerAuth();
        return instance;
    }

    @Nullable
    public String getToken() {
        if (model.getData() == null) return null;
        return model.getData().getToken();
    }

    public void setToken(String key) {
        model.setToken(key);
    }

    public boolean isAuth() {
        return model.isAuth();
    }

    @Nullable
    public String getUserId() {
        if (model.getData() == null) return null;
        return model.getData().getUserId();
    }

    public void setUserId(String id) {
        model.setUserId(id);
    }
}
