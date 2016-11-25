package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerSearchResult implements Callback<MarketplaceBlob> {

    private static ControllerSearchResult instance;
    private ArrayList<MarketplaceItem> data;
    private Call<MarketplaceBlob> request;
    private DataUpdateListener listener;

    private ControllerSearchResult() {
    }

    public static ControllerSearchResult getInstance() {
        if (instance == null) instance = new ControllerSearchResult();
        return instance;
    }

    public void setListener(DataUpdateListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public int getSize() {
        if (data == null)
            return ControllerItems.getInstance().getModel().getSize();
        return data.size();
    }

    @Nullable
    public MarketplaceItem getItem(int position) {
        if (data == null)
            return ControllerItems.getInstance().getModel().get(position);
        if (position >= 0 && position < getSize())
            return data.get(position);
        return null;
    }

    public void search(Context context, String query) {
        String token = ControllerAuth.getInstance().getToken();
        if (request != null) request.cancel();

        if (query.length() >= 2) {
            request = VkService.getApi().searchMarketItems(
                    context.getResources().getInteger(R.integer.market)
                    , query
                    , 1
                    , token
                    , context.getString(R.string.vk_api_ver)
            );
            request.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<MarketplaceBlob> call, Response<MarketplaceBlob> response) {
        if (response != null
                && response.isSuccessful()
                && response.body() != null) {
            data = response.body().getItems();
            if (data != null && data.size() == 0) data = null;
        }
        if (listener != null)
            listener.onUpdated(DataUpdateListener.TYPE_SEARCH);
    }

    @Override
    public void onFailure(Call<MarketplaceBlob> call, Throwable t) {
        if (listener != null)
            listener.onError(DataUpdateListener.TYPE_SEARCH);
    }
}
