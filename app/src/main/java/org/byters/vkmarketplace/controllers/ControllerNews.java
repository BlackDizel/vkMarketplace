package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.utils.DataUpdateListener;
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

        parseNews(items);
        ControllerStorage.getInstance().writeObjectToFile(this.data, ControllerStorage.NEWS_CACHE);

        if (listeners == null) return;
        for (DataUpdateListener listener : listeners)
            listener.onUpdated(DataUpdateListener.TYPE_NEWS);
    }

    private void parseNews(@Nullable ArrayList<NewsItem> items) {
        if (items == null) return;

        data = null;
        for (NewsItem item : items) {
            if (TextUtils.isEmpty(item.getText()) && TextUtils.isEmpty(item.getPhotoUri()))
                continue;
            if (data == null) data = new ArrayList<>();
            data.add(item);
        }
    }

    @Override
    public void onFailure(Call<NewsBlob> call, Throwable t) {
        if (listeners == null) return;
        for (DataUpdateListener listener : listeners)
            listener.onError(DataUpdateListener.TYPE_NEWS);
    }
}
