package org.byters.vkmarketplace.model.dataclasses;

public class MarketInfo {
    private String image_uri;
    private String title;
    private String address;

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MarketInfo)) return false;
        return ((MarketInfo) o).getAddress().equals(getAddress());
    }
}
