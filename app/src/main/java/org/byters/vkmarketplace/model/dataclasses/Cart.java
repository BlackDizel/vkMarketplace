package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private ArrayList<CartEntry> items;
    private String comment;

    public void addItem(int itemId) {
        if (items == null)
            items = new ArrayList<>();

        int index = -1;
        for (CartEntry entry : items)
            if (entry.getItemId() == itemId) {
                index = items.indexOf(entry);
                break;
            }

        if (index == -1)
            items.add(new CartEntry(itemId));
        else {
            CartEntry entry = items.get(index);
            entry.addItem();
            items.set(index, entry);
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemsSize() {
        return items == null ? 0 : items.size();
    }

    @Nullable
    public CartEntry getItem(int position) {
        if (items == null || position < 0 || position >= items.size()) return null;
        return items.get(position);
    }
}
