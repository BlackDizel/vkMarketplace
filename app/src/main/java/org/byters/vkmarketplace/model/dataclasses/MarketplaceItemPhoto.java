package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class MarketplaceItemPhoto implements Serializable {
    private String photo_1280;
    private String photo_130;

    public String getLittlePhoto() {
        return photo_130;
    }

    public String getSrc_big() {
        return photo_1280;
    }
}
