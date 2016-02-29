package org.byters.vkmarketplace.controllers;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.ControllerStorage;
import org.byters.vkmarketplace.controllers.controllers.utils.NewsUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.NewsBlob;
import org.byters.vkmarketplace.model.dataclasses.NewsItem;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

public class ControllerNews implements Callback<NewsBlob> {

    private ArrayList<NewsUpdateListener> listeners;

    private ArrayList<NewsItem> data;

    private ControllerMain controllerMain;

    public ControllerNews(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
        data = (ArrayList<NewsItem>) ControllerStorage.readObjectFromFile(controllerMain, ControllerStorage.NEWS_CACHE);
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public NewsItem getItem(int position) {
        if (data == null) return null;
        if (position < 0 || position >= data.size()) return null;
        return data.get(position);
    }

    public void updateData(ControllerMain context) {
        VkService.getApi()
                .getNews(context.getResources().getInteger(R.integer.market), "owner", 10, context.getString(R.string.vk_api_ver))
                .enqueue(this);
    }

    public void removeListener(NewsUpdateListener listener) {
        if (listeners != null)
            listeners.remove(listener);
    }

    public void addListener(NewsUpdateListener listener) {
        if (listener == null) return;
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    @Override
    public void onResponse(Response<NewsBlob> response) {
        NewsBlob data = response.body();
        if (data == null) return;
        NewsBlob.NewsItemsBlob news = data.getResponse();
        if (news == null) return;
        ArrayList<NewsItem> items = news.getItems();
        if (items == null) return;

        this.data = items;
        ControllerStorage.writeObjectToFile(controllerMain, this.data, ControllerStorage.NEWS_CACHE);

        if (listeners == null) return;
        for (NewsUpdateListener listener : listeners)
            listener.onUpdated(this.data);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
