package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class MarketplaceItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String thumb_photo;
    private Price price;

    public String getTitle() {
        return title;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }
}
