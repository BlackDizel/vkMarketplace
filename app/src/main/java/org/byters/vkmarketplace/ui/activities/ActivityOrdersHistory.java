package org.byters.vkmarketplace.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.ControllerOrdersHistory;
import org.byters.vkmarketplace.ui.adapters.OrdersHistoryAdapter;

public class ActivityOrdersHistory extends ActivityBase {

    OrdersHistoryAdapter adapter;

    public static void display(Context context) {
        Intent intent = new Intent(context, ActivityOrdersHistory.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvOrders = (RecyclerView) findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdersHistoryAdapter();
        rvOrders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        adapter.notifyDataSetChanged();
        int size = ControllerOrdersHistory.getInstance().getSize();

        if (size == 0) {
            findViewById(R.id.tvNoItems).setVisibility(View.VISIBLE);
            findViewById(R.id.rvOrders).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tvNoItems).setVisibility(View.GONE);
            findViewById(R.id.rvOrders).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_orders_history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_view_online:
                String uri = String.format(getString(R.string.show_orders_online_format), getString(R.string.market_id));
                ((ControllerMain) getApplicationContext()).openUrl(this, getString(R.string.action_view_browser_error), Uri.parse(uri));
                break;
            case R.id.action_clear_history:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.orders_history_clear_data_title)
                        .setPositiveButton(R.string.orders_history_clear_items_message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ControllerOrdersHistory.getInstance().clearData();
                                setData();
                            }
                        })
                        .setNegativeButton(R.string.orders_history_clear_data_cancel, null)
                        .create()
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
