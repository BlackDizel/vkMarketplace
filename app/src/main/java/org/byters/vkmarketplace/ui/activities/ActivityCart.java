package org.byters.vkmarketplace.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerCart;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.adapters.CartAdapter;

import java.util.ArrayList;

public class ActivityCart extends ActivityBase
        implements AlertDialog.OnClickListener, ItemsUpdateListener, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    EditText etComment;
    @Nullable
    CartAdapter adapter;
    @Nullable
    SwipeRefreshLayout refreshLayout;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityCart.class));
    }

    public void checkState() {
        int size = ((ControllerMain) getApplicationContext()).getControllerCart().getCartItemsSize();
        if (size == 0) {
            findViewById(R.id.tvNoItems).setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            findViewById(R.id.tvNoItems).setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
        adapter.updateData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkState();
        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
    }

    @Override
    public void onUpdated(ArrayList<MarketplaceItem> data) {
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
        checkState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter((ControllerMain) getApplicationContext());
        rvItems.setAdapter(adapter);
        rvItems.addItemDecoration(new ItemDecoration(this));

        refreshLayout = ((SwipeRefreshLayout) findViewById(R.id.srlItems));
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        if (item.getItemId() == R.id.action_send)
            tryToBuy();
        return super.onOptionsItemSelected(item);
    }

    private void tryToBuy() {

        View alertView = getLayoutInflater().inflate(R.layout.view_alert_buy, null);
        etComment = (EditText) alertView.findViewById(R.id.etComment);

        String comment = ((ControllerMain) getApplicationContext()).getControllerCart().getComment();
        if (!TextUtils.isEmpty(comment))
            etComment.setText(comment);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(alertView)
                .setTitle(R.string.alert_buy_title)
                .setPositiveButton(R.string.alert_buy_positive_button, this)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ControllerCart controllerCart = ((ControllerMain) getApplicationContext()).getControllerCart();

        if (etComment != null)
            controllerCart.setComment(this, etComment.getText().toString());

        controllerCart.trySendBuyRequest(this, findViewById(R.id.rootView));
    }

    @Override
    public void onRefresh() {
        ((ControllerMain) getApplicationContext()).updateMarketList();
    }

    //region itemDecorator
    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private int margin;

        public ItemDecoration(Context context) {
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , context.getResources().getDimension(R.dimen.view_item_cart_list_margin)
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
    //endregion
}
