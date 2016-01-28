package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class MarketplaceItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String thumb_photo;
    private Price price;

    //fixme
    public MarketplaceItem(String title) {
        this.title = title;
        this.thumb_photo = "http://lorempixel.com/200/200";
    }

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
}
