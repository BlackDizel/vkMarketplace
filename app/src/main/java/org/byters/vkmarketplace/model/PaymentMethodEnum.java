package org.byters.vkmarketplace.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.byters.vkmarketplace.R;

public enum PaymentMethodEnum {

    //todo implement payment methods
    //TYPE_PHONE(R.drawable.ic_call_black_24dp, R.string.payment_method_phone),
    TYPE_BANKCARD(R.drawable.ic_payment_white_24dp, R.string.payment_method_bankcard),
    //TYPE_VK_TRANSFER(R.drawable.ic_vk_black, R.string.payment_method_vk_transfer),
    //TYPE_PAYPAL(R.drawable.ic_paypal_black, R.string.payment_method_paypal),
    TYPE_BANK_PAYMENT(R.drawable.ic_account_balance_wallet_black_24dp, R.string.payment_method_bank_payment),
    TYPE_PRIVATE_MESSAGE(R.drawable.ic_forum_black_24dp, R.string.payment_method_private_message);

    @DrawableRes
    private final int imgRes;
    @StringRes
    private final int titleRes;

    PaymentMethodEnum(@DrawableRes int imgRes, @StringRes int titleRes) {
        this.imgRes = imgRes;
        this.titleRes = titleRes;
    }

    public int getImgRes() {
        return imgRes;
    }

    public int getTitleRes() {
        return titleRes;
    }
}
