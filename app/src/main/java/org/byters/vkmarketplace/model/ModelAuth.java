package org.byters.vkmarketplace.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.controllers.controllers.ControllerStorage;

public class ModelAuth {

    private static String data;

    public ModelAuth(Context context) {
        if (TextUtils.isEmpty(data))
            data = (String) ControllerStorage.readObjectFromFile(context, ControllerStorage.TOKEN);
    }

    public boolean isAuth() {
        return !TextUtils.isEmpty(data);
    }

    @Nullable
    public String getData() {
        return data;
    }

    public void setData(Context context, String data) {
        ModelAuth.data = data;
        ControllerStorage.writeObjectToFile(context, data, ControllerStorage.TOKEN);
    }


}
