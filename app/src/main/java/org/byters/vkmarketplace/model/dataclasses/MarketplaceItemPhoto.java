package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class MarketplaceItemPhoto implements Serializable {
    private String photo_1280;

    public String getSrc_big() {
        return photo_1280;
    }
}
