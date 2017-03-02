package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ControllerAnalytics {

    private static ControllerAnalytics instance;
    private FirebaseAnalytics analytics;

    private ControllerAnalytics() {

    }

    public static ControllerAnalytics getInstance() {
        if (instance == null) instance = new ControllerAnalytics();
        return instance;
    }

    public void init(Context context) {
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public void logItemView(int id, @Nullable String title) {

        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, id);
        if (!TextUtils.isEmpty(title))
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

    }
}
