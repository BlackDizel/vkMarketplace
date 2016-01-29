package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.model.models.ModelItems;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerItems {
    private static ModelItems model;
    private static boolean isLoading;
    private Context context;
    private ArrayList<ItemsUpdateListener> listeners;

    public ControllerItems(@NonNull Context context, @Nullable String token) {
        isLoading = false;
        this.context = context;
        if (model == null) {
            model = new ModelItems(context);
            if (model.isNull() && !TextUtils.isEmpty(token))
                getData(context.getResources().getInteger(R.integer.market), token);
        }
    }

    public void updateData(@NonNull Context context, @Nullable String token) {
        getData(context.getResources().getInteger(R.integer.market), token);
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

    public void getData(final int market, @Nullable final String token) {
        if (isLoading || TextUtils.isEmpty(token)) {
            updateListeners(null);
            return;
        }
        isLoading = true;
        new AsyncTask<Void, Void, ArrayList<MarketplaceItem>>() {
            @Override
            protected ArrayList<MarketplaceItem> doInBackground(Void... params) {
                try {
                    //fixme remove debug
                    MarketplaceBlob result = VkService.getApi().getMarketItems(market, 0, token).execute().body();
                    if (result == null) return null;
                    ArrayList<MarketplaceItem> list = result.getItems();
                    return list;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<MarketplaceItem> result) {
                super.onPostExecute(result);
                ControllerItems.this.writeData(result);
                updateListeners(result);
                isLoading = false;
            }
        }.execute();
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

}
