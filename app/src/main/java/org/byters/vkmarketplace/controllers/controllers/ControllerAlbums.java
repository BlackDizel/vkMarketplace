package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerAlbums
        implements Callback<AlbumBlob> {

    private static ControllerAlbums instance;
    private DataUpdateListener listener;
    @Nullable
    private ArrayList<AlbumBlob.AlbumItem> data;
    @Nullable
    private Call<AlbumBlob> request;

    private ControllerAlbums() {
        data = (ArrayList<AlbumBlob.AlbumItem>) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.ALBUMS_CACHE);
    }

    public static ControllerAlbums getInstance() {
        if (instance == null) instance = new ControllerAlbums();
        return instance;
    }

    private void setData(ArrayList<AlbumBlob.AlbumItem> items) {
        data = items;
        ControllerStorage.getInstance().writeObjectToFile(data, ControllerStorage.ALBUMS_CACHE);
    }


    public int getSize() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    public AlbumBlob.AlbumItem getItem(int pos) {
        if (data == null) return null;
        if (pos < 0 || pos >= data.size()) return null;
        return data.get(pos);
    }

    @Nullable
    public String getTitle(int id) {
        AlbumBlob.AlbumItem item = getItemById(id);
        if (item == null) return null;
        return item.getTitle();
    }

    @Nullable
    private AlbumBlob.AlbumItem getItemById(int id) {
        if (data == null) return null;
        for (AlbumBlob.AlbumItem item : data)
            if (item.getId() == id)
                return item;
        return null;
    }

    public void updateData(Context context) {
        String key = ControllerAuth.getInstance().getToken();
        if (!TextUtils.isEmpty(key) && (request == null || request.isExecuted())) {
            request = VkService.getApi().getAlbums(
                    context.getResources().getInteger(R.integer.market)
                    , key
                    , context.getString(R.string.vk_api_ver)
            );
            request.enqueue(this);
        }
    }

    public void removeListener() {
        listener = null;
    }

    public void setListener(DataUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<AlbumBlob> call, Response<AlbumBlob> response) {
        if (response != null && response.body() != null && response.body().getItems() != null)
            setData(response.body().getItems());

        if (listener != null)
            listener.onUpdated(DataUpdateListener.TYPE_ALBUM);
    }

    @Override
    public void onFailure(Call<AlbumBlob> call, Throwable t) {
        if (listener != null)
            listener.onError(DataUpdateListener.TYPE_ALBUM);
    }
}
