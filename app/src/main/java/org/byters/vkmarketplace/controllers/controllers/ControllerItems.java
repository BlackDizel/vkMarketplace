package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.model.models.ModelItems;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

public class ControllerItems implements Callback<MarketplaceBlob> {
    private static ModelItems model;
    private static boolean isLoading;
    private Context context;
    private ArrayList<ItemsUpdateListener> listeners;

    public ControllerItems(@NonNull Context context, @Nullable String token) {
        isLoading = false;
        this.context = context;
        if (model == null) {
            model = new ModelItems(context);
            if (model.isNull() && !TextUtils.isEmpty(token)) {
                updateData(context, token);
            }
        }
    }

    public void updateData(@NonNull Context context, @Nullable String token) {
        getData(context.getResources().getInteger(R.integer.market), token, context.getString(R.string.vk_api_ver));
    }

    //region listeners
    public void addListener(ItemsUpdateListener listener) {
        if (listener == null) return;
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void removeListener(ItemsUpdateListener listener) {
        if (listeners == null || listener == null) return;
        listeners.remove(listener);
    }
    //endregion

    public ModelItems getModel() {
        return model;
    }

    public void getData(final int market, @Nullable final String token, final String v) {
        if (isLoading || TextUtils.isEmpty(token)) {
            updateListeners(null);
            return;
        }
        isLoading = true;
        VkService.getApi().getMarketItems(market, 0, token, v).enqueue(this);
    }

    private void updateListeners(ArrayList<MarketplaceItem> data) {
        if (listeners != null)
            for (ItemsUpdateListener listener : listeners)
                if (listener != null) listener.onUpdated(data);
    }

    private void writeData(ArrayList<MarketplaceItem> result) {
        if (result != null) {
            if (context != null) model.setData(context, result);
        }
        isLoading = false;
    }

    @Override
    public void onResponse(Response<MarketplaceBlob> response) {
        ArrayList<MarketplaceItem> list = null;
        if (response != null && response.body() != null) {
            list = response.body().getItems();
            ControllerItems.this.writeData(list);
        }
        updateListeners(list);
        isLoading = false;
    }

    @Override
    public void onFailure(Throwable t) {
        updateListeners(null);
        isLoading = false;
    }
}
