package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;
import android.support.annotation.NonNull;

import org.byters.vkmarketplace.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderHistoryInfo implements Serializable {
    private long date;
    private String sum;
    private ArrayList<OrderItemHistoryInfo> items;

    public String getDate() {
        Date d = new Date();
        d.setTime(date);
        return new SimpleDateFormat("dd MMMM yyyy").format(d);
    }

    public String getSum() {
        return sum;
    }

    @NonNull
    public String getInfo(Context context) {
        if (items == null) return "";
        String result = "";
        for (OrderItemHistoryInfo item : items) {
            result = String.format("%s\n%s", result, item.getText(context));
        }
        return result;
    }

    public class OrderItemHistoryInfo implements Serializable {
        private String title;
        private int count;

        public String getText(Context context) {
            return String.format(context.getString(R.string.order_history_item_format), title, count);
        }
    }
}
