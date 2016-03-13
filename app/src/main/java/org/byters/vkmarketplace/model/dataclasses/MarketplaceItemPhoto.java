package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;

import java.io.Serializable;

public class MarketplaceItemPhoto implements Serializable {
    private String photo_1280;
    private String photo_604;
    private String photo_130;

    public String getLittlePhoto() {
        return photo_130;
    }

    public String getSrc_big(Context context) {
        if (context.getResources().getDisplayMetrics().widthPixels < 600)
            return photo_604;
        return photo_1280;
    }
}
