package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerItemInfo {
    private ArrayList<OnItemUpdateListener> listeners;
    private ArrayList<Integer> updatingIds;

    public ControllerItemInfo() {

    }

    //region listeners
    public void addItemInfoUpdatedListener(@Nullable OnItemUpdateListener listener) {
        if (listeners == null)
            listeners = new ArrayList<>();
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeItemUpdatedListener(@Nullable OnItemUpdateListener listener) {
        if (listeners == null || listener == null) return;
        listeners.remove(listener);
    }


    public void updateListeners(MarketplaceItem item) {
        if (listeners == null) return;
        for (OnItemUpdateListener listener : listeners)
            if (listener != null) listener.onItemLoaded(item);

    }

    public void updateListenersError(int itemId) {
        if (listeners == null) return;
        for (OnItemUpdateListener listener : listeners)
            if (listener != null) listener.onItemLoadError(itemId);

    }
    //endregion

    public void getItemInfo(@NonNull Context context, int id, @Nullable String token) {
        //todo check testcases
        if (updatingIds == null) updatingIds = new ArrayList<>();
        if (updatingIds.contains(id)) return;

        if (TextUtils.isEmpty(token)) {
            return;
        }

        VkService.getApi().getMarketItemsById(String.format("%d_%d", context.getResources().getInteger(R.integer.market), id)
                , 1
                , token
                , context.getString(R.string.vk_api_ver)).enqueue(new CallbackItemInfo(id));

    }


    private class CallbackItemInfo implements Callback<MarketplaceBlob> {

        private int id;

        public CallbackItemInfo(int id) {
            this.id = id;
        }

        @Override
        public void onResponse(Call<MarketplaceBlob> call, Response<MarketplaceBlob> response) {

            MarketplaceItem item = null;
            if (response != null
                    && response.body() != null
                    && response.body().getItems() != null
                    && response.body().getItems().size() > 0)
                item = response.body().getItems().get(0);

            updateListeners(item);
            updatingIds.remove((Integer) id);
        }

        @Override
        public void onFailure(Call<MarketplaceBlob> call, Throwable t) {
            updateListenersError(id);
            updatingIds.remove((Integer) id);
        }
    }

}
