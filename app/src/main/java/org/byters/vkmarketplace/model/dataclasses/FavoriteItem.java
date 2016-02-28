package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class FavoriteItem implements Serializable {
    private int id;
    private long time_added;

    public long getTime_added() {
        return time_added;
    }

    public void setTime_added(long time_added) {
        this.time_added = time_added;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
