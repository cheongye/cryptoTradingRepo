package com.example.model;


import javax.persistence.PrePersist;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;

@Entity
@Table(name = "TRADING_PAIR")
public class TradingPair {


    @Id
    @Column(updatable = false, nullable = false)
    private UUID priceId;

    @PrePersist
    protected void onCreate() {
        if (getPriceId() == null) {
            setPriceId(UUID.randomUUID());
        }
    }
    
    
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "bid_price", nullable = false)
    private double  bidPrice;
    
    @Column(name = "bid_quantity", nullable = false)
    private double  bidQuantity;
    
    @Column(name = "bidSource", nullable = false)
    private String bidSource;
    
    @Column(name = "ask_price", nullable = false)
    private double  askPrice;
    
    @Column(name = "ask_quantity", nullable = false)
    private double  askQuantity;

    @Column(name = "askSource", nullable = false)
    private String askSource;
    
    @Column(name = "priceDate", nullable = false)
    private LocalDateTime priceDate;  
    
    

    // Default constructor
    public TradingPair() {
    }
    
    
    public TradingPair(String symbol, double bidPrice,  double bidQuantity, String bidSource, double askPrice,  double askQuantity,
			String askSource, LocalDateTime priceDate) {
		this.setSymbol(symbol);
		this.setBidPrice(bidPrice);
		this.setBidQuantity(bidQuantity);
		this.setBidSource(bidSource);
		this.setAskPrice(askPrice);
		this.setAskQuantity(askQuantity) ;
		this.setAskSource(askSource);
		this.setPriceDate(priceDate);
	}



	// Constructor with parameters
    public TradingPair(String symbol) {
        this.setSymbol(symbol);
    }
 

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double  getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double  bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double  getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double  askPrice) {
        this.askPrice = askPrice;
    }

    @Override
    public String toString() {
        return "TradingPair{" +
                " symbol='" + getSymbol() + '\'' +
                ", bidSource=" + getBidSource() +
                ", bidPrice=" + getBidPrice() +
                ", bidQuantity=" + getBidQuantity() +
                ", askSource=" + getAskSource() +
                ", askQuantity=" + getAskQuantity() +
                ", askPrice=" + getAskPrice() +
                '}';
    }

	public String getAskSource() {
		return askSource;
	}

	public void setAskSource(String askSource) {
		this.askSource = askSource;
	}

	public String getBidSource() {
		return bidSource;
	}

	public void setBidSource(String bidSource) {
		this.bidSource = bidSource;
	}


	public LocalDateTime getPriceDate() {
		return priceDate;
	}


	public void setPriceDate(LocalDateTime priceDate) {
		this.priceDate = priceDate;
	}


	public double getAskQuantity() {
		return askQuantity;
	}


	public void setAskQuantity(double askQuantity) {
		this.askQuantity = askQuantity;
	}


	public double getBidQuantity() {
		return bidQuantity;
	}


	public void setBidQuantity(double bidQuantity) {
		this.bidQuantity = bidQuantity;
	}


	public UUID getPriceId() {
		return priceId;
	}


	public void setPriceId(UUID priceId) {
		this.priceId = priceId;
	}
}

