package org.byters.vkmarketplace.controllers;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.ControllerAlbums;
import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;
import org.byters.vkmarketplace.controllers.controllers.ControllerCart;
import org.byters.vkmarketplace.controllers.controllers.ControllerComments;
import org.byters.vkmarketplace.controllers.controllers.ControllerFavorites;
import org.byters.vkmarketplace.controllers.controllers.ControllerItemInfo;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.controllers.ControllerOrdersHistory;
import org.byters.vkmarketplace.controllers.controllers.ControllerSearchResult;
import org.byters.vkmarketplace.controllers.controllers.ControllerSuggestions;
import org.byters.vkmarketplace.controllers.controllers.ControllerUserData;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import retrofit2.Callback;

public class ControllerMain extends Application
        implements OnItemUpdateListener {

    private ControllerAuth controllerAuth;
    private ControllerItems controllerItems;
    private ControllerItemInfo controllerItemInfo;
    private ControllerCart controllerCart;
    private ControllerFavorites controllerFavorites;
    private ControllerNews controllerNews;
    private ControllerSearchResult controllerSearchResult;
    private ControllerUserData controllerUserData;
    private ControllerAlbums controllerAlbums;
    private ControllerComments controllerComments;
    private ControllerOrdersHistory controllerOrdersHistory;
    private ControllerSuggestions controllerSuggestions;

    public ControllerItems getControllerItems() {
        return controllerItems;
    }

    public ControllerItemInfo getControllerItemInfo() {
        return controllerItemInfo;
    }

    public ControllerCart getControllerCart() {
        return controllerCart;
    }

    public ControllerFavorites getControllerFavorites() {
        return controllerFavorites;
    }

    public ControllerNews getControllerNews() {
        return controllerNews;
    }

    public ControllerSearchResult getControllerSearchResult() {
        if (controllerSearchResult == null)
            controllerSearchResult = new ControllerSearchResult(this);
        return controllerSearchResult;
    }

    public ControllerUserData getControllerUserData() {
        if (controllerUserData == null)
            controllerUserData = new ControllerUserData(this);
        return controllerUserData;
    }

    public ControllerAlbums getControllerAlbums() {
        if (controllerAlbums == null)
            controllerAlbums = new ControllerAlbums(this);
        return controllerAlbums;
    }

    public ControllerComments getControllerComments() {
        if (controllerComments == null)
            controllerComments = new ControllerComments(this);
        return controllerComments;
    }

    public void updateAlbums() {
        getControllerAlbums().updateData(controllerAuth.getToken());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        controllerAuth = new ControllerAuth(this);
        controllerItems = new ControllerItems(this, controllerAuth.getToken());
        controllerItemInfo = new ControllerItemInfo();
        controllerItemInfo.addItemInfoUpdatedListener(this);
        controllerCart = new ControllerCart(this);
        controllerFavorites = new ControllerFavorites(this);
        controllerNews = new ControllerNews(this);
    }

    public boolean isAuth() {
        return controllerAuth.isAuth();
    }

    public void setToken(@NonNull String key) {
        controllerAuth.setToken(this, key);
        controllerItems.updateData(this, key);
        controllerNews.updateData(this);
    }

    public void setUserID(@NonNull String id) {
        controllerAuth.setUserId(this, id);
        updateUserData();
        updateAlbums();
    }

    public void updateMarketList() {
        controllerItems.updateData(this, controllerAuth.getToken());
    }

    public void updateNews() {
        controllerNews.updateData(this);
    }

    public void updateUserData() {
        getControllerUserData().updateUserData(controllerAuth.getUserId());
    }

    public void searchItems(String query) {
        getControllerSearchResult().search(query, controllerAuth.getToken());
    }

    public void updateDetailedItemInfo(int itemId, boolean isCacheAllowed) {
        MarketplaceItem item = controllerItems.getModel().getItemById(itemId);
        if (item != null && item.getPhotos() != null && isCacheAllowed)
            controllerItemInfo.updateListeners(item);
        controllerItemInfo.getItemInfo(this, itemId, controllerAuth.getToken());
    }

    @Override
    public void onItemLoaded(@NonNull MarketplaceItem item) {
        if (item == null) return;
        controllerItems.getModel().setItem(this, item);
    }

    @Override
    public void onItemLoadError(int itemId) {

    }

    @Nullable
    public String getCustomTitle() {
        if (!getControllerItems().isCustomAlbum()) return null;
        return getControllerAlbums().getTitle(getControllerItems().getAlbumId());
    }

    public void addLike(int product_id, Callback callback) {
        VkService.getApi().addLike(
                getString(R.string.market_item_like_type)
                , getResources().getInteger(R.integer.market)
                , product_id
                , controllerAuth.getToken()
                , getString(R.string.vk_api_ver)
        ).enqueue(callback);
    }

    public void isLiked(int product_id, Callback callback) {
        VkService.getApi().isLiked(
                getString(R.string.market_item_like_type)
                , getResources().getInteger(R.integer.market)
                , product_id
                , controllerAuth.getToken()
                , getString(R.string.vk_api_ver)
        ).enqueue(callback);
    }

    public Intent getIntentSendEmail(Context context, @StringRes int titleRes, @StringRes int bodyRes) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.request_buy_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(titleRes));
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(bodyRes));
        // intent.setType("message/rfc882");

        return intent;
    }

    public void getItemComments(int id) {
        getControllerComments().getComments(this, id, controllerAuth.getToken());
    }

    public void sendComment(int id, String comment) {
        getControllerComments().sendComment(this, id, comment, controllerAuth.getToken());
    }

    public void openUrl(Context context, String error_message, Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOrder(String comment, String attachment, Callback<JsonObject> callback) {
        VkService.getApi().sendMessage(
                getString(R.string.market_id)
                , comment
                , attachment
                , controllerAuth.getToken()
                , getString(R.string.vk_api_ver))
                .enqueue(callback);
    }

    public ControllerOrdersHistory getControllerOrdersHistory() {
        if (controllerOrdersHistory == null)
            controllerOrdersHistory = new ControllerOrdersHistory(this);
        return controllerOrdersHistory;
    }

    public ControllerSuggestions getControllerSuggestions() {
        if (controllerSuggestions == null)
            controllerSuggestions = new ControllerSuggestions(this);
        return controllerSuggestions;
    }
}
