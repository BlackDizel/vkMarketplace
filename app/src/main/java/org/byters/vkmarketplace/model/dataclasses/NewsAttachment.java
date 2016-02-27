package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class NewsAttachment implements Serializable {

    private static final String TYPE_PHOTO = "photo";

    private String type;
    private NewsPhoto photo;

    public NewsPhoto getPhoto() {
        return photo;
    }

    public boolean isPhoto() {
        return type.equals(TYPE_PHOTO);
    }
}
