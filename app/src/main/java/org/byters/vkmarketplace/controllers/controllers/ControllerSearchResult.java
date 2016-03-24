package org.byters.vkmarketplace.controllers.controllers;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerSearchResult implements Callback<MarketplaceBlob> {

    private ArrayList<MarketplaceItem> data;
    private ControllerMain controllerMain;
    private Call<MarketplaceBlob> request;
    private DataUpdateListener listener;

    public ControllerSearchResult(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    public void setListener(DataUpdateListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public int getSize() {
        if (data == null)
            return controllerMain.getControllerItems().getModel().getSize();
        return data.size();
    }

    @Nullable
    public MarketplaceItem getItem(int position) {
        if (data == null)
            return controllerMain.getControllerItems().getModel().get(position);
        if (position >= 0 && position < getSize())
            return data.get(position);
        return null;
    }

    public void search(String query, String token) {
        if (request != null) request.cancel();

        if (query.length() >= 2) {
            request = VkService.getApi().searchMarketItems(
                    controllerMain.getResources().getInteger(R.integer.market)
                    , query
                    , 1
                    , token
                    , controllerMain.getString(R.string.vk_api_ver)
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
