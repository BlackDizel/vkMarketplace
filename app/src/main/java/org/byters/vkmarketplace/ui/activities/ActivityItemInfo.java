package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerCart;
import org.byters.vkmarketplace.controllers.controllers.ControllerComments;
import org.byters.vkmarketplace.controllers.controllers.ControllerFavorites;
import org.byters.vkmarketplace.controllers.controllers.ControllerItemInfo;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.LikesBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.adapters.ItemInfoAdapter;
import org.byters.vkmarketplace.ui.dialogs.DialogComment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityItemInfo extends ActivityBase
        implements DataUpdateListener
        , OnItemUpdateListener
        , View.OnClickListener {

    public final static String INTENT_EXTRA_ITEM_ID = "item_id";
    private final static int NO_VALUE = -1;

    private int id;
    private ItemInfoAdapter adapter;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        id = getIntent().getIntExtra(INTENT_EXTRA_ITEM_ID, NO_VALUE);

        findViewById(R.id.fabCart).setOnClickListener(this);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvPhotos);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemInfoAdapter();
        rvItems.setAdapter(adapter);

        setData();
    }

    /**
     * called twice. on page start and on data downloaded
     */
    private void setData() {

        if (id == NO_VALUE) return;
        MarketplaceItem item = ControllerItems.getInstance().getModel().getItemById(id);
        if (item == null) return;

        if (item.getPhotos() != null
                && item.getPhotos().size() > 0 &&
                !TextUtils.isEmpty(item.getPhotos().get(0).getSrc_big(this)))
            Picasso.with(this).load(item.getPhotos().get(0).getSrc_big(this)).into((ImageView) findViewById(R.id.ivItem));
        else if (!TextUtils.isEmpty(item.getThumb_photo()))
            Picasso.with(this).load(item.getThumb_photo()).into((ImageView) findViewById(R.id.ivItem));

        adapter.updateData(item, findViewById(R.id.rootView));
    }

    //region listener
    @Override
    protected void onResume() {
        super.onResume();
        ControllerItems.getInstance().addListener(this);
        ControllerItemInfo.getInstance().addItemInfoUpdatedListener(this);
        ControllerComments.getInstance().addListener(this);

        reloadData();
        //todo state updating
    }

    @Override
    protected void onPause() {
        super.onPause();
        ControllerItems.getInstance().removeListener(this);
        ControllerItemInfo.getInstance().removeItemUpdatedListener(this);
        ControllerComments.getInstance().removeListener(this);
    }
    //endregion


    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_info_menu, menu);

        checkFav(menu.findItem(R.id.action_favorite));
        return super.onCreateOptionsMenu(menu);
    }

    private boolean checkFav(MenuItem item) {
        boolean isFav = ControllerFavorites.getInstance().isFavorite(id);
        item.setIcon(getResources().getDrawable(isFav ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp));
        return isFav;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                ControllerFavorites.getInstance().toggleFavorite(this, id);
                if (checkFav(item))
                    Snackbar.make(findViewById(R.id.rootView), R.string.activity_item_info_fav_add, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.activity_item_info_fav_add_action, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityFavorites.display(ActivityItemInfo.this);
                                }
                            })
                            .show();
                else
                    Snackbar.make(findViewById(R.id.rootView), R.string.activity_itme_info_fav_remove, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_comment:
                new DialogComment(this, findViewById(R.id.rootView), id).show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onUpdated(int type) {
        if (type == TYPE_ADD_COMMENT) {
            Snackbar.make(findViewById(R.id.rootView), R.string.activity_item_info_comment_added, Snackbar.LENGTH_SHORT).show();
            reloadData();
        }
        setData();
    }

    @Override
    public void onError(int type) {
        if (type == TYPE_ADD_COMMENT)
            Snackbar.make(findViewById(R.id.rootView), R.string.activity_item_info_comment_add_error, Snackbar.LENGTH_SHORT).show();
        //todo if no cached data, show error
        //todo else show offline mode
        setData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();

        MarketplaceItem item = ControllerItems.getInstance().getModel().getItemById(id);
        if (item != null && item.getPhotos() != null)
            ControllerItemInfo.getInstance().updateListeners(item);
        ControllerItemInfo.getInstance().getItemInfo(this, id);

        ControllerComments.getInstance().getComments(this, id);

        ((ControllerMain) getApplicationContext()).isLiked(id, new Callback<LikesBlob>() {

            @Override
            public void onResponse(Call<LikesBlob> call, Response<LikesBlob> response) {
                if (response == null) return;
                LikesBlob item = response.body();
                if (item == null) return;
                adapter.updateDataHeader(!item.isLiked());
            }

            @Override
            public void onFailure(Call<LikesBlob> call, Throwable t) {

            }
        });

    }

    @Override
    public void onItemLoaded(@NonNull MarketplaceItem item) {
        if (item == null || item.getId() != id) return;

        setData();
    }

    @Override
    public void onItemLoadError(int itemId) {
        if (itemId != id) return;

        //todo if no cached data, show error
        //todo else show offline mode
        setData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabCart && id != NO_VALUE) {
            ControllerCart.getInstance().addItem(ActivityItemInfo.this, id);
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