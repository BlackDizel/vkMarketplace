package org.byters.vkmarketplace.model;

import android.support.annotation.StringRes;

import org.byters.vkmarketplace.R;

import java.util.ArrayList;

public enum NotificationTypesEnum {
    PROMO("promo", R.string.notification_type_promo),
    SALES("sales", R.string.notification_type_sales),
    OFFLINE_MARKET("offline_market", R.string.notification_type_market),
    CONTEST("contest", R.string.notification_type_contest),
    NOTIFICATION("notification", R.string.notification_type_service);

    private final String type;
    @StringRes
    private final int titleRes;

    NotificationTypesEnum(String type, int titleRes) {
        this.type = type;
        this.titleRes = titleRes;
    }

    public static ArrayList<String> getList() {
        ArrayList<String> result = new ArrayList<>();
        for (NotificationTypesEnum item : values())
            result.add(item.getType());
        return result;
    }

    public String getType() {
        return type;
    }

    @StringRes
    public int getTitleRes() {
        return titleRes;
    }
}
