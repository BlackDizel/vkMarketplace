package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.model.models.ModelItems;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerItems {
    private static final int NO_VALUE = -1;
    private static ModelItems model;
    private Context context;
    private ArrayList<DataUpdateListener> listeners;
    private int album_id;
    private Call<MarketplaceBlob> request;

    public ControllerItems(@NonNull Context context, @Nullable String token) {
        this.context = context;
        album_id = NO_VALUE;

        if (model == null) {
            model = new ModelItems(context);
            if (model.isNull()) {
                updateData(context, token);
            }
        }
    }

    public void clearAlbum() {
        album_id = NO_VALUE;
    }

    public void setAlbum(int album_id) {
        this.album_id = album_id;
    }

    public boolean isCustomAlbum() {
        return album_id != NO_VALUE;
    }

    public int getAlbumId() {
        return album_id;
    }

    public void updateData(@NonNull Context context, @Nullable String token) {
        updateListeners();
        if (!TextUtils.isEmpty(token)) {
            if (request != null)
                request.cancel();

            if (album_id == NO_VALUE) {
                request = VkService.getApi().getMarketItems(
                        context.getResources().getInteger(R.integer.market)
                        , 0
                        , token
                        , 1
                        , context.getString(R.string.vk_api_ver));

                request.enqueue(new Callback<MarketplaceBlob>() {
                    @Override
                    public void onResponse(Response<MarketplaceBlob> response) {
                        if (response != null && response.body() != null)
                            writeData(response.body().getItems(), true);
                        updateListeners();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        updateListenersError();
                    }
                });

            } else {
                request = VkService.getApi().getMarketItems(
                        context.getResources().getInteger(R.integer.market)
                        , album_id
                        , 0
                        , token
                        , 1
                        , context.getString(R.string.vk_api_ver));

                request.enqueue(new Callback<MarketplaceBlob>() {
                    @Override
                    public void onResponse(Response<MarketplaceBlob> response) {
                        if (response != null && response.body() != null)
                            writeData(response.body().getItems(), false);
                        updateListeners();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        updateListenersError();
                    }
                });
            }
        }
    }

    //region listeners
    public void addListener(DataUpdateListener listener) {
        if (listener == null) return;
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void removeListener(DataUpdateListener listener) {
        if (listeners == null || listener == null) return;
        listeners.remove(listener);
    }
    //endregion

    public ModelItems getModel() {
        return model;
    }

    private void updateListeners() {
        if (listeners != null)
            for (DataUpdateListener listener : listeners)
                if (listener != null) listener.onUpdated(DataUpdateListener.TYPE_ITEMS);
    }

    private void updateListenersError() {
        if (listeners != null)
            for (DataUpdateListener listener : listeners)
                if (listener != null) listener.onError(DataUpdateListener.TYPE_ITEMS);
    }

    private void writeData(ArrayList<MarketplaceItem> result, boolean isCacheNeeded) {
        if (result != null) {
            if (context != null) model.setData(context, result, isCacheNeeded);
        }
    }
}
