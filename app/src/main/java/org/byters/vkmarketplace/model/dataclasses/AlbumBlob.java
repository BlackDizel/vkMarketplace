package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumBlob {

    private AlbumItemsBlob response;

    public ArrayList<AlbumItem> getItems() {
        return response == null ? null : response.getItems();
    }

    public class AlbumItemsBlob {

        private ArrayList<AlbumItem> items;

        public ArrayList<AlbumItem> getItems() {
            return items;
        }
    }

    public class AlbumItem implements Serializable {
        private int id;
        private MarketplaceItemPhoto photo;
        private String title;
        private int count;

        public int getId() {
            return id;
        }

        public MarketplaceItemPhoto getPhoto() {
            return photo;
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }
    }
}
