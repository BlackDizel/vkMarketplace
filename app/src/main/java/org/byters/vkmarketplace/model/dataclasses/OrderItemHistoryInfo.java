package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;

import org.byters.vkmarketplace.R;

import java.io.Serializable;

public class OrderItemHistoryInfo implements Serializable {
    private String title;
    private int count;

    public String getText(Context context) {
        return String.format(context.getString(R.string.order_history_item_format), title, count);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
