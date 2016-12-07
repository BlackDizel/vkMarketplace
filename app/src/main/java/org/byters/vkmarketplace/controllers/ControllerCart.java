package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.api.ServiceMarketplace;
import org.byters.vkmarketplace.api.VkService;
import org.byters.vkmarketplace.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.model.ResponsePayment;
import org.byters.vkmarketplace.model.dataclasses.Cart;
import org.byters.vkmarketplace.model.dataclasses.CartEntry;
import org.byters.vkmarketplace.model.dataclasses.OrderHistoryInfo;
import org.byters.vkmarketplace.model.dataclasses.OrderItemHistoryInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerCart {

    private static ControllerCart instance;
    DataUpdateListener listener;
    @Nullable
    private Cart cart;

    private ControllerCart() {
        cart = (Cart) ControllerStorage.getInstance().readObjectFromFile(ControllerStorage.CART_CACHE);
    }

    public static ControllerCart getInstance() {
        if (instance == null) instance = new ControllerCart();
        return instance;
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
        ControllerStorage.getInstance().writeObjectToFile(cart, ControllerStorage.CART_CACHE);
    }

    public void trySendBuyRequest(Context context, @Nullable View view) {
        if (cart == null || cart.getItemsSize() == 0) {
            if (view != null)
                Snackbar.make(view, R.string.requset_buy_error_no_products, Snackbar.LENGTH_LONG)
                        .show();
            else
                Toast.makeText(context, R.string.requset_buy_error_no_products, Toast.LENGTH_SHORT).show();
            return;
        }

        ControllerMain controllerMain = ((ControllerMain) context.getApplicationContext());

        VkService.getApi().sendMessage(
                context.getResources().getInteger(R.integer.market)
                , getRequestText(context)
                , getCartRequestAttachmentText(context)
                , ControllerAuth.getInstance().getToken()
                , context.getString(R.string.vk_api_ver))
                .enqueue(new OrderCallback(controllerMain));
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
        String result = cart.getItemsText(context, ControllerItems.getInstance().getModel());
        return result == null ? "" : result;
    }

    @Nullable
    public String getComment() {
        if (cart == null) return null;
        return cart.getComment();
    }

    public void setComment(String comment) {
        if (cart == null) cart = new Cart();
        cart.setComment(comment);
        ControllerStorage.getInstance().writeObjectToFile(cart, ControllerStorage.CART_CACHE);
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

    public int getCost() {
        if (cart == null) return 0;
        int cost = cart.getCost();
        return cart.isBonusChecked() ? Math.max(cost - ControllerBonus.getInstance().getBonusCount(), 0) : cost;
    }

    public void clearCart() {
        cart = null;
        ControllerStorage.getInstance().writeObjectToFile(cart, ControllerStorage.CART_CACHE);
    }

    public void setBonusChecked(boolean bonusChecked) {
        if (cart == null) return;
        cart.setBonusChecked(bonusChecked);
    }

    @Nullable
    public Call<ResponsePayment> getRequestBankPayment(Context context, String stripeToken) {
        if (cart == null) return null;
        return ServiceMarketplace.getApi().requestPayment(stripeToken
                , cart.getCost() * 100
                , getCartItemsText(context)
                , cart.getComment());
    }

    private class OrderCallback implements Callback<JsonObject> {
        private ControllerMain context;

        public OrderCallback(ControllerMain context) {
            this.context = context;
        }

        private void writeHistory() {
            if (cart == null) return;
            ArrayList<OrderItemHistoryInfo> items = cart.getItemsForHistory(context, ControllerItems.getInstance().getModel());
            if (items == null) return;
            OrderHistoryInfo item = new OrderHistoryInfo();
            item.setItems(items);
            item.setDate();
            item.setSum(getCost());
            ControllerOrdersHistory.getInstance().addItem(item);
        }

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            writeHistory();

            clearCart();
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
