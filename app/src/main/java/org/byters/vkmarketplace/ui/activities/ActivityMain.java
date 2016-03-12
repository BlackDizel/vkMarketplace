package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import org.byters.vkmarketplace.controllers.ControllerMain;
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
        menuAdapter = new MenuAdapter(this);
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
            case R.id.action_feedback:
                Intent intentSend = ((ControllerMain) getApplicationContext()).getIntentSendEmail(
                        this
                        , getString(R.string.feedback_message_title)
                        , getString(R.string.feedback_message_body)
                );

                if (intentSend.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentSend);
                } else {
                    Snackbar.make(findViewById(R.id.rootView), R.string.email_app_error_no_found, Snackbar.LENGTH_LONG)
                            .show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    protected void onResume() {
        super.onResume();

        if (!((ControllerMain) getApplicationContext()).isAuth())
            startActivity(new Intent(this, ActivityLogin.class));

        ((ControllerMain) getApplicationContext()).getControllerItems().addListener(this);
        ((ControllerMain) getApplicationContext()).getControllerNews().addListener(this);
        ((ControllerMain) getApplicationContext()).getControllerUserData().setListener(this);
        ((ControllerMain) getApplicationContext()).getControllerAlbums().setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerItems().removeListener(this);
        ((ControllerMain) getApplicationContext()).getControllerNews().removeListener(this);
        ((ControllerMain) getApplicationContext()).getControllerUserData().removeListener();
        ((ControllerMain) getApplicationContext()).getControllerAlbums().removeListener();
    }

    @Override
    public void onBackPressed() {
        if (((ControllerMain) getApplicationContext()).getControllerItems().isCustomAlbum()) {
            ((ControllerMain) getApplicationContext()).getControllerItems().clearAlbum();
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
        String customTitle = ((ControllerMain) getApplicationContext()).getCustomTitle();
        setTitle(!TextUtils.isEmpty(customTitle) ? customTitle : getString(R.string.app_name));

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
