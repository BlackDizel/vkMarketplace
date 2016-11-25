package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.controllers.ControllerAlbums;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.controllers.ControllerUserData;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.controllers.controllers.utils.UserInfoUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.ui.adapters.MenuAdapter;
import org.byters.vkmarketplace.ui.fragments.FragmentFeatured;

public class ActivityMain extends ActivityBase
        implements DataUpdateListener
        , UserInfoUpdateListener
        , View.OnClickListener {

    private static final String MAIN_FRAGMENT_TAG = "fragment_goods_tag";
    private MenuAdapter menuAdapter;
    private DrawerLayout drawerLayout;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityMain.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this
                , drawerLayout
                , toolbar
                , R.string.action_open_cart
                , R.string.action_open_cart);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView rvMenu = (RecyclerView) findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter();
        rvMenu.setAdapter(menuAdapter);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        Fragment f = FragmentFeatured.newInstance();
        transaction.replace(R.id.content_view, f, MAIN_FRAGMENT_TAG).commit();

        findViewById(R.id.fab).setOnClickListener(this);
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
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    protected void onResume() {
        super.onResume();

        ControllerItems.getInstance().addListener(this);
        ControllerNews.getInstance().addListener(this);
        ControllerUserData.getInstance().setListener(this);
        ControllerAlbums.getInstance().setListener(this);

        invalidateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ControllerItems.getInstance().removeListener(this);
        ControllerNews.getInstance().removeListener(this);
        ControllerUserData.getInstance().removeListener();
        ControllerAlbums.getInstance().removeListener();
    }

    @Override
    public void onBackPressed() {
        if (ControllerItems.getInstance().isCustomAlbum()) {
            ControllerItems.getInstance().clearAlbum();
            invalidateData();
        } else
            super.onBackPressed();
    }

    //region update data
    @Override
    public void onUpdated(int type) {
        if (type == TYPE_ALBUM)
            menuAdapter.updateData();
        else {
            Fragment f = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (f instanceof FragmentFeatured)
                ((FragmentFeatured) f).updateData();
        }
    }

    @Override
    public void onError(int type) {
        if (type == TYPE_ALBUM)
            menuAdapter.updateData();
        else {
            Fragment f = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (f instanceof FragmentFeatured) {
                ((FragmentFeatured) f).updateData();
                ((FragmentFeatured) f).updateWithError();
            }
        }
    }

    public void invalidateData() {
        drawerLayout.closeDrawers();
        ActivityCompat.invalidateOptionsMenu(this);
        String customTitle = !ControllerItems.getInstance().isCustomAlbum()
                ? null
                : ControllerAlbums.getInstance().getTitle(ControllerItems.getInstance().getAlbumId());
        setTitle(!TextUtils.isEmpty(customTitle) ? customTitle : getString(R.string.app_name));

        //todo set home button depends on all items or category

        Fragment f = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (f instanceof FragmentFeatured)
            ((FragmentFeatured) f).onRefresh();
    }

    @Override
    public void onUserInfoLoaded(@Nullable AccountInfo info) {
        menuAdapter.updateData();
    }

    @Override
    public void onUserInfoLoadError() {
        menuAdapter.updateData();
    }
    //endregion

    @Override
    protected void reloadData() {
        super.reloadData();
        invalidateData();
    }

    @Override
    public void onClick(View v) {
        ActivitySearch.display(this);
    }
}
