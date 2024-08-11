package com.example.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TradeHistoryDTO {
    
    private UUID tradeId; // UUID as primary key // Trade ID
    private LocalDateTime actualTradeDateTime; // Date and time when the trade was executed
    private double fromAmount; // Amount of the asset traded
    private double totalValue; // Total value of the trade (Quantity * Price)
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	public double getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}
	public LocalDateTime getActualTradeDateTime() {
		return actualTradeDateTime;
	}
	public void setActualTradeDateTime(LocalDateTime actualTradeDateTime) {
		this.actualTradeDateTime = actualTradeDateTime;
	}
	public UUID getTradeId() {
		return tradeId;
	}
	public void setTradeId(UUID tradeId) {
		this.tradeId = tradeId;
	}
	
 	
	
}
