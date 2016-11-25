package org.byters.vkmarketplace.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.byters.vkmarketplace.R;

public enum MenuEnum {

    FAVORITES(R.drawable.ic_star_white_24dp, R.string.menu_fav),
    BONUSES(R.drawable.ic_account_balance_wallet_black_24dp, R.string.menu_promo), //ввести промо-код
    SETTINGS(R.drawable.ic_settings_black_24dp, R.string.menu_settings), //настройки пушей
    CHAT(R.drawable.ic_forum_black_24dp, R.string.menu_chat),
    FEEDBACK(R.drawable.ic_thumb_up_black_24dp, R.string.menu_feedback),
    WEBSITE(R.drawable.ic_open_in_browser_white_24dp, R.string.menu_website),
    PHONE(R.drawable.ic_call_black_24dp, R.string.menu_phone),
    RATE(R.drawable.ic_favorite_white_24dp, R.string.menu_rate);

    @StringRes
    private final int menuResTitle;
    @DrawableRes
    private final int menuResDrawable;

    MenuEnum(@DrawableRes int menuResDrawable, @StringRes int menuResTitle) {
        this.menuResTitle = menuResTitle;
        this.menuResDrawable = menuResDrawable;
    }

    @StringRes
    public int getMenuResTitle() {
        return menuResTitle;
    }

    @DrawableRes
    public int getMenuResDrawable() {
        return menuResDrawable;
    }
}
