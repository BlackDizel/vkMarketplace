package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.os.AsyncTask;

import org.byters.vkmarketplace.controllers.controllers.utils.ItemsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.model.models.ModelItems;

import java.util.ArrayList;

public class ControllerItems {
    private static ModelItems model;
    private static boolean isLoading;
    private Context context;
    private ArrayList<ItemsUpdateListener> listeners;

    public ControllerItems(Context context) {
        isLoading = false;
        this.context = context;
        if (model == null) {
            model = new ModelItems(context);
            if (model.isNull())
                getData();
        }
    }

    public void addListener(ItemsUpdateListener listener) {
        if (listener == null) return;
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public ModelItems getModel() {
        return model;
    }

    public void getData() {
        if (isLoading) return;
        isLoading = true;
        new AsyncTask<Void, Void, ArrayList<MarketplaceItem>>() {
            @Override
            protected ArrayList<MarketplaceItem> doInBackground(Void... params) {
                //todo add client id check
                // try {
                //todo debug
                ArrayList<MarketplaceItem> items = new ArrayList<>();
                items.add(new MarketplaceItem("one"));
                items.add(new MarketplaceItem("two"));
                items.add(new MarketplaceItem("three"));

                return items;

                //    return VkService.getApi().getMarketItems().execute().body();
                /*} catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
*/
            }

            @Override
            protected void onPostExecute(ArrayList<MarketplaceItem> result) {
                super.onPostExecute(result);
                ControllerItems.this.writeData(result);
                if (listeners != null)
                    for (ItemsUpdateListener listener : listeners)
                        if (listener != null) listener.onUpdated(result);
            }
        }.execute();
    }


    private void writeData(ArrayList<MarketplaceItem> result) {
        if (result != null) {
            if (context != null) model.setData(context, result);
        }
        isLoading = false;
    }

}
