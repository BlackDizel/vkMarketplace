package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.ui.adapters.AdapterSettings;

public class ActivitySettings extends ActivityBase {
    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivitySettings.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();
        setList();
    }

    private void setList() {
        RecyclerView rvItems = (RecyclerView)findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(new AdapterSettings());
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