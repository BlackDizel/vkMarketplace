package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

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

    public void trySendBuyRequest() {
        //todo implement

        //todo clear cache on success

        //todo to send buy request we have 3 ways:
        //todo 1: send buy request by email app
        //todo 2: send buy request via vk api message (need additional api permission)
        //todo 3: navigate user to webPage with online form (need browser vk.com authorization)

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
}
