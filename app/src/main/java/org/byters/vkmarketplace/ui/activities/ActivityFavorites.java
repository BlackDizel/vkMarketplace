package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
    }
}
