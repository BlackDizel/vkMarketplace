package org.byters.vkmarketplace.model;

import android.support.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

public class NotificationInfo {

    private static final String FIELD_TITLE = "title";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_TEXT = "text";

    @NonNull
    private final String title;
    @NonNull
    private final String text;
    @NonNull
    private final String type;


    private NotificationInfo(@NonNull String title, @NonNull String text, @NonNull String type) {
        this.title = title;
        this.text = text;
        this.type = type;
    }

    public static NotificationInfo parse(RemoteMessage remoteMessage) {
        if (remoteMessage == null
                || remoteMessage.getData() == null
                || remoteMessage.getData().size() == 0
                || !remoteMessage.getData().containsKey(FIELD_TEXT)
                || !remoteMessage.getData().containsKey(FIELD_TITLE)
                || !remoteMessage.getData().containsKey(FIELD_TYPE))
            return null;
        return new NotificationInfo(remoteMessage.getData().get(FIELD_TITLE)
                , remoteMessage.getData().get(FIELD_TEXT)
                , remoteMessage.getData().get(FIELD_TYPE));
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public String getType() {
        return type;
    }
}
