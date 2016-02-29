package org.byters.vkmarketplace.model.dataclasses;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsItem implements Serializable {
    ArrayList<NewsAttachment> attachments;
    private String text;
    private int id;
    private MarketplaceItem.LikeInfo likes;

    public MarketplaceItem.LikeInfo getLikes() {
        return likes;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public ArrayList<NewsAttachment> getAttachments() {
        return attachments;
    }

    @Nullable
    public String getPhotoUri() {
        if (attachments == null) return null;
        for (NewsAttachment attachment : attachments) {
            if (attachment == null) continue;
            if (!attachment.isPhoto()) continue;
            NewsPhoto photo = attachment.getPhoto();
            if (photo == null) continue;
            String uri = photo.getUri();
            if (TextUtils.isEmpty(uri))
                continue;
            return uri;
        }
        return null;
    }
}
