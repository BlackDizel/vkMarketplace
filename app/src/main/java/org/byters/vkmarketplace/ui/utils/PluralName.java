package org.byters.vkmarketplace.ui.utils;

import android.content.Context;

import org.byters.vkmarketplace.R;


public enum PluralName {
    ITEM(R.string.plural_one_item, R.string.plural_several_item, R.string.plural_many_item);

    private final int one;
    private final int several;
    private final int many;

    private PluralName(int one, int several, int many) {
        this.one = one;
        this.several = several;
        this.many = many;
    }

    public String toString(Context context, int val) {

        String one = context.getString(this.one);
        String several = context.getString(this.several);
        String many = context.getString(this.many);
        return PluralStrings.getStringForm(val, one, several, many);
    }


}

