package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.controllers.controllers.utils.NewsUpdateListener;
import org.byters.vkmarketplace.ui.fragments.FragmentFeatured;
import org.byters.vkmarketplace.ui.fragments.FragmentGoods;

import java.util.ArrayList;

public class ActivityMain extends ActivityBase
        implements ItemsUpdateListener
        , NewsUpdateListener
        , TabLayout.OnTabSelectedListener {

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityMain.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initTabs();
    }

    private void initTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_featured)), true);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_goods)));
    }

    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                ActivityCart.display(this);
                break;
            case R.id.action_view_market:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.market_address)));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Snackbar.make(findViewById(R.id.rootView), R.string.action_view_browser_error, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_favorites_show:
                ActivityFavorites.display(this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!((ControllerMain) getApplicationContext()).isAuth())
            startActivity(new Intent(this, ActivityLogin.class));

        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
        ((ControllerMain) getApplicationContext()).getControllerNews().addListener(this);
    }
    //endregion

    //region update data
    @Override
    public void onUpdated(ArrayList data) {
        //todo update fragments data
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
        ((ControllerMain) getApplicationContext()).getControllerNews().removeListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //todo try to add animation
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (tab.getText().equals(getString(R.string.tab_featured))) {
            transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
            Fragment f = FragmentFeatured.newInstance();
            transaction.replace(R.id.content_view, f).commit();
        } else if (tab.getText().equals(getString(R.string.tab_goods))) {
            transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            Fragment f = FragmentGoods.newInstance();
            transaction.replace(R.id.content_view, f).commit();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
