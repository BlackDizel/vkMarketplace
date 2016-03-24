package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.JsonObject;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.dataclasses.Cart;
import org.byters.vkmarketplace.model.dataclasses.CartEntry;
import org.byters.vkmarketplace.model.dataclasses.OrderHistoryInfo;
import org.byters.vkmarketplace.model.dataclasses.OrderItemHistoryInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerCart {

    DataUpdateListener listener;
    @Nullable
    private Cart cart;

    public ControllerCart(Context context) {
        cart = (Cart) ControllerStorage.readObjectFromFile(context, ControllerStorage.CART_CACHE);
    }

    public void setListener(DataUpdateListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public void addItem(Context context, int itemId) {
        if (cart == null) cart = new Cart();
        cart.addItem(itemId);
        ControllerStorage.writeObjectToFile(context, cart, ControllerStorage.CART_CACHE);
    }

    public void trySendBuyRequest(Context context, View view) {
        if (cart == null || cart.getItemsSize() == 0) {
            Snackbar.make(view, R.string.requset_buy_error_no_products, Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        ControllerMain controllerMain = ((ControllerMain) context.getApplicationContext());
        controllerMain.sendOrder(
                getRequestText(context)
                , getCartRequestAttachmentText(context)
                , new OrderCallback(controllerMain));
    }

    private String getCartRequestAttachmentText(Context context) {
        if (cart == null || cart.getItemsSize() == 0) return "";
        String result = cart.getRequestText(context);
        return result == null ? "" : result;
    }

    @NonNull
    private String getRequestText(Context context) {
        if (cart == null) return "";
        String comment = cart.getComment();
        String result = String.format("%s\n%s", comment == null ? "" : comment, getCartItemsText(context));
        return result;
    }

    @NonNull
    private String getCartItemsText(Context context) {
        if (cart == null || cart.getItemsSize() == 0) return "";
        String result = cart.getItemsText(context, ((ControllerMain) context.getApplicationContext()).getControllerItems().getModel());
        return result == null ? "" : result;
    }

    public void setComment(Context context, String comment) {
        if (cart == null) cart = new Cart();
        cart.setComment(comment);
        ControllerStorage.writeObjectToFile(context, cart, ControllerStorage.CART_CACHE);
    }

    @Nullable
    public String getComment() {
        if (cart == null) return null;
        return cart.getComment();
    }

    public int getCartItemsSize() {
        return cart == null ? 0 : cart.getItemsSize();
    }

    @Nullable
    public CartEntry getCartItem(int position) {
        return cart == null ? null : cart.getItem(position);
    }

    @Nullable
    public CartEntry getCartItemById(int id) {
        return cart == null ? null : cart.getItemById(id);
    }

    public int getCartItemPosition(int id) {
        return cart == null ? Cart.NO_VALUE : cart.getItemPosition(id);
    }

    public void removeItem(int pos) {
        if (cart == null) return;
        cart.removeItem(pos);
    }

    public int getCost(ControllerItems controllerItems) {
        if (cart == null) return 0;
        return cart.getCost(controllerItems);
    }

    public void clearCart(Context context) {
        cart = null;
        ControllerStorage.writeObjectToFile(context, cart, ControllerStorage.CART_CACHE);
    }

    private class OrderCallback implements Callback<JsonObject> {
        private ControllerMain context;

        public OrderCallback(ControllerMain context) {
            this.context = context;
        }

        private void writeHistory() {
            if (cart == null) return;
            ArrayList<OrderItemHistoryInfo> items = cart.getItemsForHistory(context, ((ControllerMain) context.getApplicationContext()).getControllerItems().getModel());
            if (items == null) return;
            OrderHistoryInfo item = new OrderHistoryInfo();
            item.setItems(items);
            item.setDate();
            item.setSum(context.getControllerCart().getCost(context.getControllerItems()));
            context.getControllerOrdersHistory().addItem(context, item);
        }

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            writeHistory();

            clearCart(context);
            if (listener != null)
                listener.onUpdated(DataUpdateListener.TYPE_CART_ORDER_SENT);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            if (listener != null)
                listener.onError(DataUpdateListener.TYPE_CART_ORDER_SENT);
        }
    }
}
