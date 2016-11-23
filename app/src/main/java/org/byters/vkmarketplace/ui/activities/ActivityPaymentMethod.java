package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.ui.fragments.FragmentPaymentMethod;

public class ActivityPaymentMethod extends ActivityBase {

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityPaymentMethod.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        setToolbar();
        setData();
    }

    private void setData() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, new FragmentPaymentMethod())
                .commit();
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
}
