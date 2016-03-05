package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.models.ModelAuth;

public class ControllerAuth {

    private ModelAuth model;

    public ControllerAuth(Context context) {
        model = new ModelAuth(context);
    }

    @Nullable
    public String getToken() {
        if (model.getData() == null) return null;
        return model.getData().getToken();
    }

    public boolean isAuth() {
        return model.isAuth();
    }

    public void setToken(Context context, String key) {
        model.setToken(context, key);
    }

    public void setUserId(Context context, String id) {
        model.setUserId(context, id);
    }

    @Nullable
    public String getUserId() {
        if (model.getData() == null) return null;
        return model.getData().getUserId();
    }
}
