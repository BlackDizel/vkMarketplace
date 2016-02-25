package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

public class ActivityItemInfo extends ActivityBase
        implements ItemsUpdateListener, OnItemUpdateListener, View.OnClickListener {

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

        findViewById(R.id.fabCart).setOnClickListener(this);

        setData();
    }

    /**
     * called twice. on page start and on data downloaded
     */
    private void setData() {
        //todo add viewpager with photos

        if (id == NO_VALUE) return;
        MarketplaceItem item = ((ControllerMain) getApplicationContext()).getControllerItems().getModel().getItemById(id);
        if (item == null) return;

        setTitle(item.getTitle());

        ((TextView) findViewById(R.id.tvDescription)).setText(Html.fromHtml(item.getDescription()));
        ((TextView) findViewById(R.id.tvPrice)).setText(item.getPrice().getText());

        if (item.getPhotos() != null
                && item.getPhotos().size() > 0 &&
                !TextUtils.isEmpty(item.getPhotos().get(0).getSrc_big()))
            ImageLoader.getInstance().displayImage(item.getPhotos().get(0).getSrc_big(), (ImageView) findViewById(R.id.ivItem));
        else if (!TextUtils.isEmpty(item.getThumb_photo()))
            ImageLoader.getInstance().displayImage(item.getThumb_photo(), (ImageView) findViewById(R.id.ivItem));
    }

    //region listener
    @Override
    protected void onResume() {
        super.onResume();
        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
        ((ControllerMain) getApplicationContext()).getControllerItemInfo().addItemInfoUpdatedListener(this);

        ((ControllerMain) getApplicationContext()).updateDetailedItemInfo(id);
        //todo state updating
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
        ((ControllerMain) getApplicationContext()).getControllerItemInfo().removeItemUpdatedListener(this);
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

    @Override
    public void onItemLoaded(@NonNull MarketplaceItem item) {
        //todo add check error state
        if (item.getId() != id) return;

        //todo state idle
        setData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabCart && id != NO_VALUE) {
            ((ControllerMain) getApplicationContext()).getControllerCart().addItem(ActivityItemInfo.this, id);
            Snackbar.make(findViewById(R.id.rootView), R.string.item_added_to_cart, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_open_cart, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCart.display(ActivityItemInfo.this);
                        }
                    })
                    .show();
        }
    }
}