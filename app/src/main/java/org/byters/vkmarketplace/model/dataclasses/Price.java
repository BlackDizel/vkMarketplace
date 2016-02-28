package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class Price implements Serializable {
    private int amount;
    private String text;

    @Nullable
    public String getText() {
        return text;
    }

    public int getAmount() {
        return amount;
    }
}
