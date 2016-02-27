package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class NewsPhoto implements Serializable {
    private String photo_75;
    private String photo_130;
    private String photo_604;
    private String photo_807;
    private String photo_1280;
    private String text;

    public String getUri() {
        return photo_604;
    }
}
