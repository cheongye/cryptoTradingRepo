package com.example.model;

import java.io.Serializable;

public class UserWalletId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userId;
    private String cryptocurrency;

    public UserWalletId() {}

    public UserWalletId(String userId, String cryptocurrency) {
        this.userId = userId;
        this.cryptocurrency = cryptocurrency;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(String cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }
}
