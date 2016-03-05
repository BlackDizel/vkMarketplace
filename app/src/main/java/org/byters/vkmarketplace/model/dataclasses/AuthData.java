package org.byters.vkmarketplace.model.dataclasses;

import java.io.Serializable;

public class AuthData implements Serializable {
    private String token;
    private String userId;

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
