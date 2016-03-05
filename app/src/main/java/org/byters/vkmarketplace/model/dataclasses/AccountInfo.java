package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class AccountInfo implements Serializable {
    private String first_name;
    private String last_name;
    private String photo_max_orig;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto_max_orig() {
        return photo_max_orig;
    }
}
