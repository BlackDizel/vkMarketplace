package org.byters.vkmarketplace.ui.utils;

import android.support.annotation.LayoutRes;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;

public class ShopHelper {

    @LayoutRes
    public static int getItemLayout() {
        if (BuildConfig.shopItemType == BuildConfig.shopItemTypeVertical)
            return R.layout.view_list_items_vertical;
        else
            return R.layout.view_list_items_overlay;
    }

}
