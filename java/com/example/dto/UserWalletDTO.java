package com.example.dto;

public class UserWalletDTO {

    private String userId;
    private double balance;
    private String cryptocurrency;

    public UserWalletDTO() {
    }

    public UserWalletDTO(String id, String cryptocurrency, double balance) {
        this.userId = id;
        this.setCryptocurrency(cryptocurrency);
        this.balance = balance;
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

	public String getCryptocurrency() {
		return cryptocurrency;
	}

	public void setCryptocurrency(String cryptocurrency) {
		this.cryptocurrency = cryptocurrency;
	}
}
