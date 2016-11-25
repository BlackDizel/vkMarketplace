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
import org.byters.vkmarketplace.controllers.controllers.ControllerComments;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.controllers.ControllerSearchResult;
import org.byters.vkmarketplace.controllers.controllers.ControllerStorage;
import org.byters.vkmarketplace.controllers.controllers.ControllerUserData;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import retrofit2.Callback;

public class ControllerMain extends Application
        implements OnItemUpdateListener {

    @Override
    public void onCreate() {
        super.onCreate();
        ControllerStorage.getInstance().setContext(this);
    }

    public void setUserID(Context context, @NonNull String id) {
        ControllerAuth.getInstance().setUserId(id);
        ControllerUserData.getInstance().updateUserData(context);
        ControllerAlbums.getInstance().updateData(this);
    }

    @Override
    public void onItemLoaded(@NonNull MarketplaceItem item) {
        if (item == null) return;
        ControllerItems.getInstance().getModel().setItem(item);
    }

    @Override
    public void onItemLoadError(int itemId) {

    }

    public void addLike(int product_id, Callback callback) {
        VkService.getApi().addLike(
                getString(R.string.market_item_like_type)
                , getResources().getInteger(R.integer.market)
                , product_id
                , ControllerAuth.getInstance().getToken()
                , getString(R.string.vk_api_ver)
        ).enqueue(callback);
    }

    public void isLiked(int product_id, Callback callback) {
        VkService.getApi().isLiked(
                getString(R.string.market_item_like_type)
                , getResources().getInteger(R.integer.market)
                , product_id
                , ControllerAuth.getInstance().getToken()
                , getString(R.string.vk_api_ver)
        ).enqueue(callback);
    }

    @NonNull
    public Intent getIntentSendEmail(Context context, @StringRes int titleRes, @StringRes int bodyRes) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.request_buy_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(titleRes));
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(bodyRes));
        // intent.setType("message/rfc882");

        return intent;
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

    public void call(Context context, @StringRes int errorRes, @StringRes int phoneRes) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(context.getString(phoneRes))));

        if (intent.resolveActivity(getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, errorRes, Toast.LENGTH_SHORT).show();
        }
    }
}
