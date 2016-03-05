package org.byters.vkmarketplace.controllers.controllers.utils;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.dataclasses.AccountInfo;

public interface UserInfoUpdateListener {
    void onUpdated(@Nullable AccountInfo info);

}
