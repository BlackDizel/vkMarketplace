package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerAlbums;
import org.byters.vkmarketplace.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.ControllerUserData;
import org.byters.vkmarketplace.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.controllers.utils.UserInfoUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.ui.fragments.FragmentFeatured;

public class ActivityMain extends ActivityBase
        implements DataUpdateListener
        , UserInfoUpdateListener
        , View.OnClickListener
        , NavigationView.OnNavigationItemSelectedListener {

    private static final String MAIN_FRAGMENT_TAG = "fragment_goods_tag";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityMain.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setToolbar();
        navigateMain();
        findViewById(R.id.fab).setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setMenuToggle(toolbar);
    }

    private void navigateMain() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        Fragment f = FragmentFeatured.newInstance();
        transaction.replace(R.id.content_view, f, MAIN_FRAGMENT_TAG).commit();
    }

    private void setMenuToggle(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this
                , drawerLayout
                , toolbar
                , R.string.action_open_cart
                , R.string.action_open_cart);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
        if (type != TYPE_ALBUM) {
            Fragment f = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (f instanceof FragmentFeatured)
                ((FragmentFeatured) f).updateData();
        }
    }

    @Override
    public void onError(int type) {
        if (type != TYPE_ALBUM) {
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
        updateMenuHeader();
    }

    @Override
    public void onUserInfoLoadError() {
        updateMenuHeader();
    }
    //endregion

    private void updateMenuHeader() {
        ImageView imgView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivHeader);
        TextView tvTitle = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvTitle);

        AccountInfo info = ControllerUserData.getInstance().getData();
        if (info == null) {
            imgView.setImageDrawable(null);
            tvTitle.setText("");
        } else {
            Picasso.with(this).load(info.getPhoto_max_orig()).into(imgView);
            tvTitle.setText(String.format("%s %s", info.getFirst_name(), info.getLast_name()));
        }
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        invalidateData();
    }

    @Override
    public void onClick(View v) {
        ActivitySearch.display(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_nav_shop:
                navigateMain();
                break;
            case R.id.action_nav_fav:
                ActivityFavorites.display(this);
                break;
            /*case R.id.action_nav_bonus:
                ActivityBonus.display(this);
                break;*/
            case R.id.action_nav_history:
                ActivityOrdersHistory.display(this);
                break;
            case R.id.action_nav_settings:
                ActivitySettings.display(this);
                break;
            case R.id.action_nav_chat:
                String uri = String.format(getString(R.string.show_orders_online_format), getResources().getInteger(R.integer.market));
                ControllerMain.openUrl(this, getString(R.string.action_view_browser_error), Uri.parse(uri));
                //todo implement app chat activity
                //ActivityChat.display(itemView.getContext());
                break;
            case R.id.action_nav_feedback:
                sendEmail();
                break;
            case R.id.action_nav_website:
                ((ControllerMain) getApplicationContext()).openUrl(this
                        , getString(R.string.action_view_browser_error)
                        , Uri.parse(getString(R.string.market_address)));
                break;
            case R.id.action_nav_phone:
                ((ControllerMain) getApplicationContext()).call(this
                        , R.string.calling_error
                        , R.string.market_phone);
                break;
            case R.id.action_nav_rate:
                ((ControllerMain) getApplicationContext()).openUrl(this
                        , getString(R.string.action_view_browser_error)
                        , Uri.parse(String.format(getString(R.string.play_market_address_format), BuildConfig.APPLICATION_ID)));
                break;
        }
        return false;
    }

    private void sendEmail() {
        Intent intentSend = ((ControllerMain) getApplicationContext()).getIntentSendEmail(this
                , R.string.feedback_message_title
                , R.string.feedback_message_body);

        if (intentSend.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, R.string.email_app_error_no_found, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intentSend);
    }
}
