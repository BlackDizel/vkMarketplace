package org.byters.vkmarketplace.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerBonus;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.ui.adapters.CartAdapter;
import org.byters.vkmarketplace.ui.dialogs.DialogPayment;

public class ActivityCart extends ActivityBase
        implements
        DataUpdateListener
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener {

    @Nullable
    EditText etComment;
    @Nullable
    CartAdapter adapter;
    @Nullable
    SwipeRefreshLayout refreshLayout;
    @Nullable
    View contentView;
    TextView tvCost;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityCart.class));
    }

    public void checkState() {
        int size = ((ControllerMain) getApplicationContext()).getControllerCart().getCartItemsSize();
        if (size == 0) {
            findViewById(R.id.tvNoItems).setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } else {
            ControllerMain controllerMain = ((ControllerMain) getApplicationContext());
            int cost = controllerMain.getControllerCart().getCost(controllerMain.getControllerItems());
            tvCost.setText(String.format(getString(R.string.cart_general_cost_format), cost));
            setBonus();

            findViewById(R.id.tvNoItems).setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
        adapter.updateData();
    }

    private void setBonus() {
        int bonusCount = ControllerBonus.getInstance().getBonusCount();

        findViewById(R.id.tvBonusCount).setVisibility(bonusCount == 0 ? View.GONE : View.VISIBLE);
        findViewById(R.id.cbBonus).setVisibility(bonusCount == 0 ? View.GONE : View.VISIBLE);

        ((TextView) findViewById(R.id.tvBonusCount))
                .setText(String.format(getString(R.string.cart_bonus_format), bonusCount));
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkState();
        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
        ((ControllerMain) getApplicationContext()).getControllerCart().setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
        ((ControllerMain) getApplicationContext()).getControllerCart().removeListener();
    }

    @Override
    public void onUpdated(int type) {
        if (type == TYPE_CART_ORDER_SENT) {
            Snackbar.make(findViewById(R.id.rootView), R.string.activity_cart_order_sent, Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            if (refreshLayout != null)
                refreshLayout.setRefreshing(false);
        }
        checkState();
    }

    @Override
    public void onError(int type) {
        if (type == TYPE_CART_ORDER_SENT) {
            Snackbar.make(findViewById(R.id.rootView), R.string.email_app_error_no_found, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            //todo if no cached data, show error
            //todo else show offline mode
            if (refreshLayout != null)
                refreshLayout.setRefreshing(false);
        }
        checkState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contentView = findViewById(R.id.contentView);
        tvCost = (TextView) findViewById(R.id.tvCost);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter((ControllerMain) getApplicationContext());
        rvItems.setAdapter(adapter);

        ControllerMain controllerMain = ((ControllerMain) getApplicationContext());
        controllerMain.getControllerCart().setBonusChecked(false);

        refreshLayout = ((SwipeRefreshLayout) findViewById(R.id.srlItems));
        refreshLayout.setOnRefreshListener(this);

        findViewById(R.id.tvPayment).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_cart_clear:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.cart_clear_dialog_text)
                        .setPositiveButton(R.string.cart_clear_dialog_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ControllerMain) getApplicationContext()).getControllerCart()
                                        .clearCart(ActivityCart.this);
                                checkState();
                            }
                        })
                        .setNegativeButton(R.string.cart_clear_dialog_cancel, null)
                        .create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onRefresh() {
        ((ControllerMain) getApplicationContext()).getControllerItems().clearAlbum();
        ((ControllerMain) getApplicationContext()).updateMarketList();
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.paymentType == BuildConfig.paymentTypeAll) {

            boolean isBonusChecked = ((CheckBox) findViewById(R.id.cbBonus)).isChecked();

            ControllerMain controllerMain = ((ControllerMain) getApplicationContext());
            controllerMain.getControllerCart().setBonusChecked(isBonusChecked);
            int cost = controllerMain.getControllerCart().getCost(controllerMain.getControllerItems());

            if (cost == 0) {
                //todo implement
            } else
                ActivityPaymentMethod.display(this);

        } else if (BuildConfig.paymentType == BuildConfig.paymentTypeMessages)
            new DialogPayment(this, findViewById(R.id.rootView)).show();
    }
}
