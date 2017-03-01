package org.byters.vkmarketplace.model;

import java.util.ArrayList;

public enum NotificationTypesEnum {
    PROMO("promo"),
    SALES("sales"),
    OFFLINE_MARKET("offline_market"),
    CONTEST("contest"),
    NOTIFICATION("notification");

    private final String type;

    NotificationTypesEnum(String type) {
        this.type = type;
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
}
