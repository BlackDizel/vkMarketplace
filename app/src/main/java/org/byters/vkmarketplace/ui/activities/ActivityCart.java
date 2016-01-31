package org.byters.vkmarketplace.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerCart;

public class ActivityCart extends ActivityBase implements AlertDialog.OnClickListener {

    @Nullable
    EditText etComment;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityCart.class));
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

        controllerCart.trySendBuyRequest();
    }
}
