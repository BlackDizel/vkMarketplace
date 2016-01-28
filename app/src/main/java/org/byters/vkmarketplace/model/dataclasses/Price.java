package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class Price implements Serializable {
    private int amount;
    private String text;

    public String getText() {
        return text;
    }
}
