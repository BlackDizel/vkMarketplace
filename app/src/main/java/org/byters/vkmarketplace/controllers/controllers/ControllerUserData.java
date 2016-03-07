package org.byters.vkmarketplace.controllers.controllers;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkApi;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.UserInfoUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.model.dataclasses.AccountInfoBlob;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerUserData implements Callback<AccountInfoBlob> {
    Call<AccountInfoBlob> request;
    private ControllerMain controllerMain;
    private UserInfoUpdateListener listener;

    private AccountInfo data;

    public ControllerUserData(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
        data = (AccountInfo) ControllerStorage.readObjectFromFile(controllerMain, ControllerStorage.USERINFO_CACHE);

    }

    public void updateUserData(@Nullable String userId) {
        if ((request == null || request.isExecuted())
                && !TextUtils.isEmpty(userId)) {
            request = VkService.getApi().getAccountInfo(userId, VkApi.USER_INFO_FIELDS, controllerMain.getString(R.string.vk_api_ver));
            request.enqueue(this);
        }
    }

    @Nullable
    public AccountInfo getData() {
        return data;
    }

    private void setData(@Nullable AccountInfo info) {
        this.data = info;
        if (controllerMain != null)
            ControllerStorage.writeObjectToFile(controllerMain, info, ControllerStorage.USERINFO_CACHE);
    }

    public void setListener(UserInfoUpdateListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void onResponse(Response<AccountInfoBlob> response) {
        if (response != null
                && response.body() != null
                && response.body().getResponse() != null)
            setData(response.body().getResponse());

        if (listener != null) listener.onUserInfoLoaded(data);
    }

    @Override
    public void onFailure(Throwable t) {
        if (listener != null) listener.onUserInfoLoaded(null);
    }
}
