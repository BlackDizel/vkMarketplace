package org.byters.vkmarketplace.model.models;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.controllers.ControllerStorage;
import org.byters.vkmarketplace.model.dataclasses.AuthData;

public class ModelAuth {

    private static AuthData data;

    public ModelAuth() {
        if (data == null)
            data = (AuthData) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.TOKEN);
    }

    public boolean isAuth() {
        return data != null && !TextUtils.isEmpty(data.getToken());
    }

    @Nullable
    public AuthData getData() {
        return data;
    }

    public void setData(AuthData data) {
        ModelAuth.data = data;
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.TOKEN);
    }

    public void setToken(String key) {
        if (ModelAuth.data == null) ModelAuth.data = new AuthData();
        data.setToken(key);
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.TOKEN);
    }

    public void setUserId(String id) {
        if (ModelAuth.data == null) ModelAuth.data = new AuthData();
        data.setUserId(id);
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.TOKEN);
    }
}
