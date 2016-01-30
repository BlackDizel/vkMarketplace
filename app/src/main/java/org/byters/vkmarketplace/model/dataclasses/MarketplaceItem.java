package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class MarketplaceItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String thumb_photo;
    private Price price;
    private ArrayList<MarketplaceItemPhoto> photos;

    public ArrayList<MarketplaceItemPhoto> getPhotos() {
        return photos;
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

    public int getId() {
        return id;
    }
}
