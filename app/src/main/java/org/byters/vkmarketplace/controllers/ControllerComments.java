package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.CommentsBlob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerComments {

    private static ControllerComments instance;
    private ArrayList<DataUpdateListener> listeners;
    private Call<CommentsBlob> request;
    private Map<Integer, ArrayList<CommentsBlob.CommentInfo>> data;

    private ControllerComments() {

    }

    public static ControllerComments getInstance() {
        if (instance == null) instance = new ControllerComments();
        return instance;
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


    @Nullable
    public ArrayList<CommentsBlob.CommentInfo> getComments(int id) {
        if (data == null) return null;
        return data.get(id);
    }

    private void addItem(int id, @NonNull ArrayList<CommentsBlob.CommentInfo> data) {
        if (this.data == null)
            this.data = new HashMap<>();

        this.data.put(id, data);
    }

    public void getComments(Context context, int id) {
        if (request != null)
            request.cancel();

        String token = ControllerAuth.getInstance().getToken();
        request = VkService.getApi().getMarketItemsComments(
                context.getResources().getInteger(R.integer.market)
                , id
                , 0
                , "desc"
                , 1
                , "first_name,last_name,photo_100"
                , token
                , context.getString(R.string.vk_api_ver)
        );
        request.enqueue(new CommentsCallback(id));
    }

    public int getSize(int id) {
        if (data == null) return 0;
        ArrayList<CommentsBlob.CommentInfo> items = getComments(id);
        if (items == null) return 0;
        return items.size();
    }

    @Nullable
    public CommentsBlob.CommentInfo getCommentsItem(int id, int pos) {
        if (data == null) return null;
        ArrayList<CommentsBlob.CommentInfo> items = data.get(id);
        if (items == null) return null;
        if (pos < 0 || pos >= items.size()) return null;
        return items.get(pos);
    }

    public void sendComment(Context context, int id, String comment) {
        String token = ControllerAuth.getInstance().getToken();
        VkService.getApi().createComment(
                context.getResources().getInteger(R.integer.market)
                , id
                , comment
                , token
                , context.getString(R.string.vk_api_ver)
        ).enqueue(new AddCommentsCallback());
    }

    private class CommentsCallback
            implements Callback<CommentsBlob> {

        private int id;

        public CommentsCallback(int id) {
            this.id = id;
        }

        @Override
        public void onResponse(Call<CommentsBlob> call, Response<CommentsBlob> response) {
            if (response != null && response.body() != null && response.body().getComments() != null)
                addItem(id, response.body().getComments());

            if (listeners != null)
                for (DataUpdateListener listener : listeners)
                    if (listener != null)
                        listener.onUpdated(DataUpdateListener.TYPE_COMMENTS);
        }

        @Override
        public void onFailure(Call<CommentsBlob> call, Throwable t) {
            if (listeners != null)
                for (DataUpdateListener listener : listeners)
                    if (listener != null)
                        listener.onError(DataUpdateListener.TYPE_COMMENTS);

        }

    }

    private class AddCommentsCallback implements Callback<com.google.gson.JsonObject> {

        @Override
        public void onResponse(Call<JsonObject> call, Response<com.google.gson.JsonObject> response) {
            if (listeners != null)
                for (DataUpdateListener listener : listeners)
                    if (listener != null)
                        listener.onUpdated(DataUpdateListener.TYPE_ADD_COMMENT);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            if (listeners != null)
                for (DataUpdateListener listener : listeners)
                    if (listener != null)
                        listener.onError(DataUpdateListener.TYPE_ADD_COMMENT);
        }
    }

}
