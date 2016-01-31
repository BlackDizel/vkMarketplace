package org.byters.vkmarketplace.controllers.controllers;

import android.content.Context;

import org.byters.vkmarketplace.model.dataclasses.Cart;

public class ControllerCart {

    private Cart cart;

    public ControllerCart(Context context) {
        //todo try to load cart from cache
    }

    public void addItem(int itemId) {
        if (cart == null) cart = new Cart();
        cart.addItem(itemId);
        //todo save cache
    }


}
