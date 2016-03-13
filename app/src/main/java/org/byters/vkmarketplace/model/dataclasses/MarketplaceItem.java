package org.byters.vkmarketplace.model.dataclasses;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class MarketplaceItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String thumb_photo;
    private Price price;
    private ArrayList<MarketplaceItemPhoto> photos;
    private int[] albums_ids;
    private LikeInfo likes;

    public ArrayList<MarketplaceItemPhoto> getPhotos() {
        return photos;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public String getDescription() {
        return description;
    }

    public int getAlbumIdsSize() {
        return albums_ids == null ? 0 : albums_ids.length;
    }

    public Price getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int getPhotosSize() {
        return photos == null ? 0 : photos.size();
    }

    public LikeInfo getLikes() {
        return likes;
    }

    @Nullable
    public String getPhotoByPosition(Context context, int position) {
        if (photos == null) return null;
        if (position < 0 || position >= photos.size()) return null;
        return photos.get(position).getSrc_big(context);
    }

    public class LikeInfo implements Serializable {
        private int user_likes;
        private int count;

        public int getUser_likes() {
            return user_likes;
        }

        public int getCount() {
            return count;
        }
    }
}
