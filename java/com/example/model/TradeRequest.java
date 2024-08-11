package com.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TradeRequest {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID tradeRequestId; // UUID as primary key // Trade ID

    private String userId; // Trade user
    
    
    private String fromCurrency; // Trading pair or asset
    private double fromAmt; // from amount of the asset traded
    private String toCurrency; // Trading pair or asset
//    private double price; // Price at which the asset was traded
    private double totalValue; // Total value of the trade (Quantity * Price)
    private LocalDateTime actualTradeDateTime; // Date and time when the trade was executed
    private LocalDateTime requestedTradeDateTime; // Date and time when the trade was requested
    private String tradeType; // Type of trade (buy or sell)
    
    // Constructor
    public TradeRequest() {
        this.tradeRequestId = UUID.randomUUID(); // Generate UUID
    }
    
    
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public LocalDateTime getRequestedTradeDateTime() {
		return requestedTradeDateTime;
	}
	public void setRequestedTradeDateTime(LocalDateTime requestedTradeDateTime) {
		this.requestedTradeDateTime = requestedTradeDateTime;
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
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
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
	public UUID getTradeRequestId() {
		return tradeRequestId;
	}
	public void setTradeRequestId(UUID tradeRequestId) {
		this.tradeRequestId = tradeRequestId;
	}
	public double getFromAmt() {
		return fromAmt;
	}
	public void setFromAmt(double fromAmt) {
		this.fromAmt = fromAmt;
	}
    
}
 