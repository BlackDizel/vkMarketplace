package org.byters.vkmarketplace.model.dataclasses;

import java.util.ArrayList;

public class NewsBlob {
    private NewsItemsBlob response;

    public NewsItemsBlob getResponse() {
        return response;
    }

    public class NewsItemsBlob {
        private ArrayList<NewsItem> items;

        public ArrayList<NewsItem> getItems() {
            return items;
        }
    }
}
