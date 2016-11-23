package org.byters.vkmarketplace.controllers;

import android.support.annotation.Nullable;

import org.byters.vkmarketplace.model.PaymentMethodEnum;

public class ControllerListPaymentMethod {
    private static ControllerListPaymentMethod instance;

    public static ControllerListPaymentMethod getInstance() {
        if (instance == null) instance = new ControllerListPaymentMethod();
        return instance;
    }

    public int getItemsCount() {
        return PaymentMethodEnum.values().length;
    }

    @Nullable
    public PaymentMethodEnum getItem(int position) {
        return position < 0 || position >= getItemsCount() ? null : PaymentMethodEnum.values()[position];
    }
}
