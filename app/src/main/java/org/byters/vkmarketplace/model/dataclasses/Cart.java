package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
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
    public String getItemsText(Context context, ModelItems model) {
        if (getItemsSize() == 0) return null;
        String result = null;
        for (CartEntry item : items) {
            if (result == null) result = "";
            MarketplaceItem marketInfo = model.getItemById(item.getItemId());
            String marketInfoText;
            if (marketInfo == null) marketInfoText = "";
            else marketInfoText = marketInfo.getTitle();
            result = String.format(context.getString(R.string.request_order_item_format)
                    , result, marketInfoText, item.getQuantity());
        }
        return result;
    }

    @Nullable
    public ArrayList<OrderItemHistoryInfo> getItemsForHistory(Context context, ModelItems model) {
        if (getItemsSize() == 0) return null;
        ArrayList<OrderItemHistoryInfo> result = null;
        for (CartEntry item : items) {
            if (result == null) result = new ArrayList<>();
            MarketplaceItem marketInfo = model.getItemById(item.getItemId());
            String marketInfoText;
            if (marketInfo == null) marketInfoText = "";
            else marketInfoText = marketInfo.getTitle();

            OrderItemHistoryInfo historyInfo = new OrderItemHistoryInfo();
            historyInfo.setTitle(marketInfoText);
            historyInfo.setCount(item.getQuantity());
            result.add(historyInfo);
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

    public int getCost(ControllerItems controllerItems) {
        if (items == null) return 0;
        int sum = 0;
        for (CartEntry item : items) {
            MarketplaceItem mItem = controllerItems.getModel().getItemById(item.getItemId());
            if (mItem == null) continue;
            int cost = mItem.getPrice().getAmount() / 100;
            sum += item.getQuantity() * cost;
        }
        return sum;
    }

    public String getRequestText(Context context) {
        if (getItemsSize() == 0) return null;
        String result = null;
        for (CartEntry item : items) {
            if (result == null) result = "";

            result = String.format(context.getString(R.string.attachment_format)
                    , result
                    , context.getString(R.string.market_id)
                    , item.getItemId());
        }
        return result;
    }
}
