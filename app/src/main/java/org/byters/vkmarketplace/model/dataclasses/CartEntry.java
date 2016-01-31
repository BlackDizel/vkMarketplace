package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class CartEntry implements Serializable {
    private int itemId;
    private int quantity;

    public CartEntry(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public CartEntry(int itemId) {
        this.itemId = itemId;
        this.quantity = 1;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addItem() {
        ++quantity;
    }
}
