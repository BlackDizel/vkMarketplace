package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class AccountInfoBlob {
    private ArrayList<AccountInfo> response;

    @Nullable
    public AccountInfo getResponse() {
        if (response == null || response.size() == 0)
            return null;
        return response.get(0);
    }
}
