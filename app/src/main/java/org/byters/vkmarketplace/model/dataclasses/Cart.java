package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.models.ModelItems;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    public static final int NO_VALUE = -1;
    private ArrayList<CartEntry> items;
    @Nullable
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

    @Nullable
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

    @Nullable
    public String getItemsText(ModelItems model) {
        if (getItemsSize() == 0) return null;
        String result = null;
        for (CartEntry item : items) {
            if (result == null) result = "";
            MarketplaceItem marketInfo = model.getItemById(item.getItemId());
            String marketInfoText;
            if (marketInfo == null) marketInfoText = "";
            else marketInfoText = marketInfo.getTitle();
            result = String.format("%s\n%d: %s, %d"
                    , result, item.getItemId(), marketInfoText, item.getQuantity());
        }
        return result;
    }

    @Nullable
    public CartEntry getItemById(int id) {
        if (items == null) return null;
        for (CartEntry item : items)
            if (item.getItemId() == id)
                return item;
        return null;
    }

    public int getItemPosition(int id) {
        if (items == null) return NO_VALUE;
        for (CartEntry item : items)
            if (item.getItemId() == id)
                return items.indexOf(item);
        return NO_VALUE;
    }

    public void removeItem(int position) {
        if (items == null || position < 0 || position >= items.size()) return;
        items.remove(position);
    }
}
