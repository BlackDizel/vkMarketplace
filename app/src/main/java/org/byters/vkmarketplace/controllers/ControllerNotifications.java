package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.byters.vkmarketplace.model.NotificationInfo;
import org.byters.vkmarketplace.model.NotificationTypesEnum;

import java.util.ArrayList;

public class ControllerNotifications {
    public static final String TOPICS_TYPE_ALL = "ALL";
    private static ControllerNotifications instance;

    private ControllerNotifications() {
    }

    public static ControllerNotifications getInstance() {
        if (instance == null) instance = new ControllerNotifications();
        return instance;
    }

    @Nullable
    public NotificationInfo getNotificationInfo(Context context, RemoteMessage remoteMessage) {
        NotificationInfo info = NotificationInfo.parse(remoteMessage);
        if (info == null || !isNotificationTypeEnabled(context, info.getType())) return null;
        return info;
    }

    private boolean isNotificationTypeEnabled(Context context, String type) {
        ArrayList<String> types = ControllerStorage.getInstance().getNotificationTypesEnabled(context);
        return types != null && types.contains(type);
    }

    void subscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPICS_TYPE_ALL);
    }

    public void initNotificationTypes() {
        ControllerStorage.getInstance().setNotificationTypesEnabled(NotificationTypesEnum.getList());
    }
}
