package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.adapters.ItemsAdapter;

import java.util.ArrayList;

public class ActivityMain extends ActivityBase implements ItemsUpdateListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!((ControllerMain) getApplicationContext()).isAuth())
            startActivity(new Intent(this, ActivityLogin.class));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlItems);
        refreshLayout.setOnRefreshListener(this);

        adapter = new ItemsAdapter((ControllerMain) getApplicationContext());

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvItems);
        int columns = getResources().getInteger(R.integer.items_list_columns);
        rv.setLayoutManager(new GridLayoutManager(this, columns));
        rv.addItemDecoration(new ItemDecoration(this));
        rv.setAdapter(adapter);
    }

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

    //region update data
    @Override
    public void onUpdated(ArrayList<MarketplaceItem> data) {
        if (refreshLayout != null) refreshLayout.setRefreshing(false);
        if (adapter != null && data != null) adapter.updateData();
    }

    @Override
    public void onRefresh() {
        ((ControllerMain)getApplicationContext()).updateMarketList();
    }
    //endregion

    //region itemDecorator
    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private int margin;

        public ItemDecoration(Context context) {
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , context.getResources().getDimension(R.dimen.view_item_list_margin)
                    , context.getResources().getDisplayMetrics());

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildLayoutPosition(view);

            outRect.top = 2 * margin;
            if (position % 2 == 1) {
                outRect.right = 2 * margin;
                outRect.left = margin;
            } else {
                outRect.right = margin;
                outRect.left = 2 * margin;

                //margins sum = const
            }

        }
    }
    //endregion

}
