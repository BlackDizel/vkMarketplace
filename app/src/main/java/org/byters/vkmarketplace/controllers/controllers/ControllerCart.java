package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.Cart;
import org.byters.vkmarketplace.model.dataclasses.CartEntry;

public class ControllerCart {

    @Nullable
    private Cart cart;

    public ControllerCart(Context context) {
        cart = (Cart) ControllerStorage.readObjectFromFile(context, ControllerStorage.CART_CACHE);
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

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.request_buy_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.request_buy_email_title));
        intent.putExtra(Intent.EXTRA_TEXT, getRequestText(context));
        intent.setType("message/rfc882");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            cart = null;
            //todo update cache on app closing
            ControllerStorage.writeObjectToFile(context, cart, ControllerStorage.CART_CACHE);
            context.startActivity(intent);
        } else {
            Snackbar.make(view, R.string.request_buy_error_no_app_found, Snackbar.LENGTH_LONG)
                    .show();
        }
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
        String result = cart.getItemsText(((ControllerMain) context.getApplicationContext()).getControllerItems().getModel());
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
}
