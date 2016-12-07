package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerBonus;

public class ActivityBonus extends ActivityBase
        implements View.OnClickListener {
    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityBonus.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        setToolbar();
        findViewById(R.id.ivScan).setOnClickListener(this);
        setData();
    }

    private void setData() {
        int bonusCount = ControllerBonus.getInstance().getBonusCount();
        ((TextView) findViewById(R.id.tvPromoSum)).setText(String.valueOf(bonusCount));
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        //todo implement scan QR promo code
    }
}
