package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.byters.vkmarketplace.R;

public class ActivityItemInfo extends ActivityBase {

    public final static String INTENT_EXTRA_ITEM_ID = "item_id";
    private final static int NO_VALUE = -1;

    private int id;

    public static void display(Context context, int id) {
        Intent intent = new Intent(context, ActivityItemInfo.class);
        intent.putExtra(ActivityItemInfo.INTENT_EXTRA_ITEM_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        id = getIntent().getIntExtra(INTENT_EXTRA_ITEM_ID, NO_VALUE);
    }
}
