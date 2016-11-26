package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkApi;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.utils.UserInfoUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.model.dataclasses.AccountInfoBlob;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerUserData implements Callback<AccountInfoBlob> {
    private static ControllerUserData instance;
    Call<AccountInfoBlob> request;
    private UserInfoUpdateListener listener;
    private AccountInfo data;

    private ControllerUserData() {
        data = (AccountInfo) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.USERINFO_CACHE);
    }

    public static ControllerUserData getInstance() {
        if (instance == null) instance = new ControllerUserData();
        return instance;
    }

    public void updateUserData(Context context) {
        String userId = ControllerAuth.getInstance().getUserId();
        if ((request == null || request.isExecuted())
                && !TextUtils.isEmpty(userId)) {
            request = VkService.getApi().getAccountInfo(userId, VkApi.USER_INFO_FIELDS, context.getString(R.string.vk_api_ver));
            request.enqueue(this);
        }
    }

    @Nullable
    public AccountInfo getData() {
        return data;
    }

    private void setData(@Nullable AccountInfo info) {
        this.data = info;
        ControllerStorage.getInstance().writeObjectToFile(info, ControllerStorage.USERINFO_CACHE);
    }

    public void setListener(UserInfoUpdateListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void onResponse(Call<AccountInfoBlob> call, Response<AccountInfoBlob> response) {
        if (response != null
                && response.body() != null
                && response.body().getResponse() != null)
            setData(response.body().getResponse());

        if (listener != null) listener.onUserInfoLoaded(data);
    }

    @Override
    public void onFailure(Call<AccountInfoBlob> call, Throwable t) {
        if (listener != null) listener.onUserInfoLoadError();
    }
}
