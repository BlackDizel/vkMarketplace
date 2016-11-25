package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.NewsBlob;
import org.byters.vkmarketplace.model.dataclasses.NewsItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerNews implements Callback<NewsBlob> {

    private static ControllerNews instance;
    private ArrayList<DataUpdateListener> listeners;

    private ArrayList<NewsItem> data;

    private ControllerNews() {
        data = (ArrayList<NewsItem>) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.NEWS_CACHE);
    }

    public static ControllerNews getInstance() {
        if (instance == null) instance = new ControllerNews();
        return instance;
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public NewsItem getItem(int position) {
        if (data == null) return null;
        if (position < 0 || position >= data.size()) return null;
        return data.get(position);
    }

    public void updateData(Context context) {
        VkService.getApi()
                .getNews(context.getResources().getInteger(R.integer.market), "owner", 10, context.getString(R.string.vk_api_ver))
                .enqueue(this);
    }

    public void removeListener(DataUpdateListener listener) {
        if (listeners != null)
            listeners.remove(listener);
    }

    public void addListener(DataUpdateListener listener) {
        if (listener == null) return;
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    @Override
    public void onResponse(Call<NewsBlob> call, Response<NewsBlob> response) {
        NewsBlob data = response.body();
        if (data == null) return;
        NewsBlob.NewsItemsBlob news = data.getResponse();
        if (news == null) return;
        ArrayList<NewsItem> items = news.getItems();
        if (items == null) return;

        this.data = items;
        ControllerStorage.getInstance().writeObjectToFile(this.data, ControllerStorage.NEWS_CACHE);

        if (listeners == null) return;
        for (DataUpdateListener listener : listeners)
            listener.onUpdated(DataUpdateListener.TYPE_NEWS);
    }

    @Override
    public void onFailure(Call<NewsBlob> call, Throwable t) {
        if (listeners == null) return;
        for (DataUpdateListener listener : listeners)
            listener.onError(DataUpdateListener.TYPE_NEWS);
    }
}
