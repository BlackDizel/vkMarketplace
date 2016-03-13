package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderHistoryInfo implements Serializable {
    private long date;
    private int sum;
    private ArrayList<OrderItemHistoryInfo> items;

    public String getDate() {
        Date d = new Date();
        d.setTime(date);
        return new SimpleDateFormat("dd MMMM yyyy").format(d);
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
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

    public void setDate() {
        date = System.currentTimeMillis();
    }

    public void setItems(ArrayList<OrderItemHistoryInfo> items) {
        this.items = items;
    }

}
