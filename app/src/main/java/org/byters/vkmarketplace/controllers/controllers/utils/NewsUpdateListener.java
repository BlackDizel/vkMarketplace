package org.byters.vkmarketplace.controllers.controllers.utils;

import org.byters.vkmarketplace.model.dataclasses.NewsItem;

import java.util.ArrayList;

public interface NewsUpdateListener {
    void onUpdated(ArrayList<NewsItem> data);
}
