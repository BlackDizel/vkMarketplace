package org.byters.vkmarketplace.controllers.utils;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.dataclasses.AccountInfo;

public interface UserInfoUpdateListener {
    void onUserInfoLoaded(@Nullable AccountInfo info);

    void onUserInfoLoadError();
}
