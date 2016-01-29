package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public class ActivityItemInfo extends ActivityBase implements ItemsUpdateListener {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getIntExtra(INTENT_EXTRA_ITEM_ID, NO_VALUE);
        setData();
    }

    private void setData() {
        if (id == NO_VALUE) return;
        MarketplaceItem item = ((ControllerMain) getApplicationContext()).getControllerItems().getModel().getItemById(id);
        if (item == null) return;
        ((TextView) findViewById(R.id.tvDebug)).setText(item.getTitle());
        ImageLoader.getInstance().displayImage("http://lorempixel.com/400/300", (ImageView) findViewById(R.id.ivItem));
    }

    //region listener
    @Override
    protected void onResume() {
        super.onResume();
        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdated(ArrayList<MarketplaceItem> data) {
        if (data == null) return;
        setData();
    }
}
