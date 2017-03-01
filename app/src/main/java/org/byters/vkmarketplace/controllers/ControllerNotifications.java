package org.byters.vkmarketplace.controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.byters.vkmarketplace.model.NotificationInfo;
import org.byters.vkmarketplace.model.NotificationTypesEnum;

import java.util.ArrayList;

public class ControllerNotifications {
    public static final String TOPICS_TYPE_ALL = "ALL";
    private static ControllerNotifications instance;
    private ArrayList<String> data;

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

    void initNotificationTypes() {
        ControllerStorage.getInstance().setNotificationTypesEnabled(NotificationTypesEnum.getList());
    }

    public int getNotificationTypesNum() {
        return NotificationTypesEnum.values().length;
    }

    public boolean isNotificationTypeEnabled(int position) {
        updateData();
        return data != null && data.contains(NotificationTypesEnum.values()[position].getType());
    }

    private void updateData() {
        if (data == null) data = ControllerStorage.getInstance().getNotificationTypesEnabled();
    }

    public void setNotificationTypeEnabled(int position, boolean isChecked) {
        updateData();
        if (!isChecked) {
            if (data == null) return;
            data.remove(NotificationTypesEnum.values()[position].getType());
        } else {
            if (data == null) data = new ArrayList<>();
            if (data.contains(NotificationTypesEnum.values()[position].getType())) return;
            data.add(NotificationTypesEnum.values()[position].getType());
        }
        ControllerStorage.getInstance().setNotificationTypesEnabled(data);
    }

    @StringRes
    public int getNotificationTypeTitleResource(int position) {
        return NotificationTypesEnum.values()[position].getTitleRes();
    }
}
