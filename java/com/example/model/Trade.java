package com.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID tradeId; // UUID as primary key // Trade ID
    
    
    private UUID tradeRequestId; // UUID as primary key // Trade ID

    private String userId; // Trade user
    private String fromCurrency; // Trading pair or asset
    private String toCurrency; // Trading pair or asset
    private double fromAmount; // Amount of the asset traded
    private double price; // Price at which the asset was traded
    private double totalValue; // Total value of the trade (Quantity * Price)
    private LocalDateTime actualTradeDateTime; // Date and time when the trade was executed
    private String tradeType; // Type of trade (buy or sell)
 
    
    // Default constructor
    public Trade() {
        this.setActualTradeDateTime(LocalDateTime.now()); // Initialize tradeDate to the current date and time by default
    }


	public String getTradeType() {
		return tradeType;
	}


	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}


	public LocalDateTime getActualTradeDateTime() {
		return actualTradeDateTime;
	}


	public void setActualTradeDateTime(LocalDateTime actualTradeDateTime) {
		this.actualTradeDateTime = actualTradeDateTime;
	}


	public double getTotalValue() {
		return totalValue;
	}


	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getFromAmount() {
		return fromAmount;
	}


	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}


	public String getFromCurrency() {
		return fromCurrency;
	}


	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public UUID getTradeId() {
		return tradeId;
	}


	public void setTradeId(UUID tradeId) {
		this.tradeId = tradeId;
	}

	public String getToCurrency() {
		return toCurrency;
	}


	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}


	public UUID getTradeRequestId() {
		return tradeRequestId;
	}


	public void setTradeRequestId(UUID tradeRequestId) {
		this.tradeRequestId = tradeRequestId;
	}
    
    
    
    
    
    
    
    
}
