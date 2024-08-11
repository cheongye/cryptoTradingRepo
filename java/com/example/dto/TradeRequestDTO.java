package com.example.dto;


public class TradeRequestDTO {
	private String symbol; // e.g., "ETHUSDT"
	private double quantity; // Quantity to buy or sell
	private String tradeType; // Type of trade: "BUY" or "SELL"



	private String fromCurrency;
	private String toCurrency;
	private double fromAmountToTrade;


	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public double getFromAmountToTrade() {
		return fromAmountToTrade;
	}

	public void setFromAmountToTrade(double fromAmountToTrade) {
		this.fromAmountToTrade = fromAmountToTrade;
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
}
