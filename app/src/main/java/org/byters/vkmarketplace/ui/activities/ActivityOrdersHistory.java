package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.byters.vkmarketplace.R;

public class ActivityOrdersHistory extends ActivityBase {

    public static void display(Context context) {
        Intent intent = new Intent(context, ActivityOrdersHistory.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);
    }

    //todo implement
}
