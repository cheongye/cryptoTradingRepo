package com.example.dto;


public class PriceDataDTO {

    private double  bidPrice;
    private double  askPrice;
    
    private double  bidQuantity;
    private double  askQuantity;
    private String askSource ;
    private String bidSource ;

    public PriceDataDTO(double  bidPrice,double  bidQuantity, double  askPrice, double  askQuantity, String askSource, String bidSource) {
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.setBidQuantity(bidQuantity);
        this.setAskQuantity(askQuantity);
        this.setAskSource(askSource);
        this.setBidSource(bidSource) ;
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

	public String getSource() {
		return getAskSource();
	}

	public void setSource(String source) {
		this.setAskSource(source);
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
	
	
	public String getBidSource() {
		return bidSource;
	}
	
	public void setBidSource(String bidSource) {
		this.bidSource = bidSource;
	}

    @Override
    public String toString() {
        return "PriceDataDTO{" +
                "bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ", bidQuantity=" + bidQuantity +
                ", askQuantity=" + askQuantity +
                ", bidSource=" + bidSource +
                ", askSource='" + getAskSource() + '\'' +
                '}';
    }

	public String getAskSource() {
		return askSource;
	}

	public void setAskSource(String askSource) {
		this.askSource = askSource;
	}
}

