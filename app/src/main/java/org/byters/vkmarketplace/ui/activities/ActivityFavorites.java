package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.ui.adapters.FavoritesAdapter;

public class ActivityFavorites extends ActivityBase {

    FavoritesAdapter adapter;

    public static void display(Context context) {
        Intent intent = new Intent(context, ActivityFavorites.class);
        context.startActivity(intent);
    }

    public void checkState() {
        int size = ((ControllerMain) getApplicationContext()).getControllerFavorites().getSize();
        if (size == 0) {
            findViewById(R.id.tvNoItems).setVisibility(View.VISIBLE);
            findViewById(R.id.rvItems).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tvNoItems).setVisibility(View.GONE);
            findViewById(R.id.rvItems).setVisibility(View.VISIBLE);
        }
        adapter.updateData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter((ControllerMain) getApplicationContext());
        rvItems.setAdapter(adapter);
        rvItems.addItemDecoration(new ItemDecoration(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
    }
    //endregion

    //region itemDecorator
    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private int margin;

        public ItemDecoration(Context context) {
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , context.getResources().getDimension(R.dimen.view_item_fav_list_margin)
                    , context.getResources().getDisplayMetrics());
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.left = margin;
            outRect.top = margin;
            outRect.right = margin;
        }
    }
}
