package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.OnItemUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.io.IOException;
import java.util.ArrayList;

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
    //endregion

    public void getItemInfo(@NonNull Context context, int id, @Nullable String token) {
        //todo check testcases
        if (updatingIds == null) updatingIds = new ArrayList<>();
        if (updatingIds.contains(id)) return;

        if (TextUtils.isEmpty(token)) {
            return;
        }

        new DetailedItemInfoDownloader(id
                , String.format("%d_%d", context.getResources().getInteger(R.integer.market), id)
                , token).execute();
    }

    private class DetailedItemInfoDownloader extends AsyncTask<Void, Void, MarketplaceItem> {

        private String ids;
        private String token;
        private int id;

        public DetailedItemInfoDownloader(int id, String ids, String token) {
            this.ids = ids;
            this.token = token;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updatingIds.add(id);
        }

        @Override
        protected MarketplaceItem doInBackground(Void... params) {
            try {
                MarketplaceBlob result = VkService.getApi().getMarketItemsById(ids, 1, token).execute().body();
                if (result == null || result.getItems().size() == 0) return null;
                return result.getItems().get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MarketplaceItem item) {
            super.onPostExecute(item);
            //todo write item to cache

            updateListeners(item);
            updatingIds.remove((Integer) id); //todo check situation no onpostexecute called
        }
    }


}
