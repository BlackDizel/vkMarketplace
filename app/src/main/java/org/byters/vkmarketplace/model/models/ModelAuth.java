package org.byters.vkmarketplace.model.models;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.controllers.controllers.ControllerStorage;
import org.byters.vkmarketplace.model.dataclasses.AuthData;

public class ModelAuth {

    private static AuthData data;

    public ModelAuth(Context context) {
        if (data == null)
            data = (AuthData) ControllerStorage.readObjectFromFile(context, ControllerStorage.TOKEN);
    }

    public boolean isAuth() {
        if (data == null) return false;
        return !TextUtils.isEmpty(data.getToken());
    }

    @Nullable
    public AuthData getData() {
        return data;
    }

    public void setData(Context context, AuthData data) {
        ModelAuth.data = data;
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.TOKEN);
    }

    public void setToken(Context context, String key) {
        if (ModelAuth.data == null) ModelAuth.data = new AuthData();
        data.setToken(key);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.TOKEN);
    }

    public void setUserId(Context context, String id) {
        if (ModelAuth.data == null) ModelAuth.data = new AuthData();
        data.setUserId(id);
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.TOKEN);
    }
}
