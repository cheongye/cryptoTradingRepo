package com.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.repository.TradeRepository;
import com.example.repository.TradeRequestRepository;
import com.example.repository.UserWalletRepository;
import com.example.util.CryptoApiProperties;


import com.example.dto.PriceDataDTO;
import com.example.model.Trade;
import com.example.model.TradeRequest;
import com.example.model.TradingPair;
import com.example.model.UserWallet;

@Service
@Transactional
public class TradeService {
	private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	private UserWalletService userWalletService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private TradeRepository tradeRepository;

	@Autowired
	private TradeRequestRepository tradeRequestRepository;

	@Autowired
	private UserWalletRepository userWalletRepository;


	@Autowired
	private CryptoApiProperties cryptoApiProperties;

	public  final String CONST_SELL = "SELL";
	public final String CONST_BUY = "BUY";
	public final String CONST_USDT = "USDT";

	private List<String> allowedSymbols ;


	@PostConstruct
	public void init() {
		this.allowedSymbols= cryptoApiProperties.getAllowedSymbols();
	}


	public String executeTrade(String fromCurrency, String toCurrency, double fromAmountToTrade, String tradeType, String userId) {


		// Basic validation
		if (fromAmountToTrade <= 0) {
			throw new IllegalArgumentException("Amount to trade must be greater than zero");
		}
		if (!allowedSymbols.contains(fromCurrency) || !allowedSymbols.contains(toCurrency)) {
			throw new IllegalArgumentException("Invalid currency. Must be " + String.join(",", allowedSymbols));

		}
		if (!tradeType.equalsIgnoreCase(CONST_BUY) && !tradeType.equalsIgnoreCase(CONST_SELL)) {
			throw new IllegalArgumentException("Type must be either 'buy' or 'sell'");
		}

		double remainingAmountToTrade = fromAmountToTrade;
		TradeRequest tradeRequest = new TradeRequest();

		tradeRequest.setFromCurrency(fromCurrency);
		tradeRequest.setToCurrency(toCurrency);
		tradeRequest.setTradeType(tradeType);
		tradeRequest.setFromAmt(fromAmountToTrade);
		tradeRequest.setRequestedTradeDateTime(LocalDateTime.now());
		tradeRequest.setUserId(userId);

		// Get or create the FROM wallet
		UserWallet fromWallet = userWalletRepository.findByUserIdAndCryptocurrency(userId, fromCurrency)
				.orElseThrow(() -> new RuntimeException("Wallet for currency " + fromCurrency + " not found"));

		if(fromWallet.getBalance() < fromAmountToTrade) {
			//not enough balance, throw error;
			logger.error("Insufficient {} balance", fromCurrency);
			throw new RuntimeException("Insufficient " + fromCurrency + " balance");
		}

		String lookForSymbol = toCurrency +  fromCurrency ;

		switch (tradeType.toUpperCase()) {
		case CONST_BUY:
			if(fromCurrency.equals(CONST_USDT)) {
				lookForSymbol = toCurrency + CONST_USDT;
			}
			break;
		case CONST_SELL:
			if(toCurrency.equals(CONST_USDT)) {
				lookForSymbol = fromCurrency + CONST_USDT;
			}
			break;
		default:
			break;
		}
		
        double totalConvertedAmount =0 ;


		while (remainingAmountToTrade > 0) {
			List<TradingPair> priceDataList = priceService.getBestPrices();
	
			logger.info("price data list element " + priceDataList.size());
			
			logger.info("remainingAmountToTrade" + remainingAmountToTrade);
			boolean tradeCompleted = false;

			for (TradingPair priceData : priceDataList) {

				if (priceData.getSymbol().equals(lookForSymbol)) 
				{
					double price = tradeType.equalsIgnoreCase(CONST_BUY) ? priceData.getAskPrice() : priceData.getBidPrice();
					double availableQuantity = tradeType.equalsIgnoreCase(CONST_BUY) ? priceData.getAskQuantity() : priceData.getBidQuantity();

					if (remainingAmountToTrade <= 0) {
						break;
					}

		            double amountToTradeAtCurrentPrice = Math.min(remainingAmountToTrade, availableQuantity * price);

		            double convertedAmount = tradeType.equalsIgnoreCase(CONST_BUY) ? amountToTradeAtCurrentPrice / price : amountToTradeAtCurrentPrice * price;

		            
					logger.info("price" + price);
					logger.info("availableQuantity" + availableQuantity);
					logger.info("amountToTradeAtCurrentPrice" + amountToTradeAtCurrentPrice);
					logger.info("convertedAmount" + convertedAmount);
					
					updateWallet(userId, tradeType,  fromCurrency, toCurrency, convertedAmount, -amountToTradeAtCurrentPrice);

					// Save the trade record
					Trade trade = new Trade();
					trade.setUserId(userId);
					
					logger.info("test trade requestID" + tradeRequest.getTradeRequestId());
					
					//					trade.setSymbol(fromCurrency + "/" + toCurrency);
					trade.setFromCurrency(fromCurrency);
					trade.setFromAmount(amountToTradeAtCurrentPrice);
					trade.setToCurrency(toCurrency);
					trade.setTradeType(tradeType.toUpperCase());
					trade.setTotalValue(convertedAmount);
					trade.setActualTradeDateTime(LocalDateTime.now()); // Set the current date
					trade.setTradeRequestId(tradeRequest.getTradeRequestId());
					Trade savedTrade = tradeRepository.save(trade);

					/*
					 * // Save trade request to trade_request table TradeRequest tradeRequest = new
					 * TradeRequest(); tradeRequest.setUserId(userId);
					 * tradeRequest.setSymbol(fromCurrency + "/" + toCurrency);
					 * tradeRequest.setQuantity(amountToTradeAtCurrentPrice);
					 * tradeRequest.setTradeType(tradeType.toUpperCase());
					 * tradeRequest.setPrice(price); tradeRequest.setDate(new Date());
					 * tradeRequest.setTrade(savedTrade); // Link TradeRequest to Trade
					 * tradeRequests.add(tradeRequest);
					 */
					
					
					remainingAmountToTrade -= amountToTradeAtCurrentPrice;
					totalConvertedAmount +=  convertedAmount;
					tradeCompleted = true;

					tradeRequest.setActualTradeDateTime(LocalDateTime.now());
					tradeRequest.setTotalValue(remainingAmountToTrade);
					// Break if the remaining amount is processed
					if (remainingAmountToTrade <= 0) {
						break;
					}
				}
			}

			if (!tradeCompleted) {
				throw new RuntimeException("Unable to fulfill the entire trade amount. Remaining amount: " + remainingAmountToTrade);
			}
		}

		// Batch save trade requests
		tradeRequest.setTotalValue(totalConvertedAmount);
		tradeRequestRepository.save(tradeRequest);

		return "Trade executed successfully";


	}


	private void updateWallet(String userId, String tradeType, String fromCurrency, String toCurrency, double convertedAmount, double amountToTradeAtCurrentPrice) {

		logger.info("0. updateWallet ");
		List <UserWallet> walletA = userWalletRepository.findByUserId(userId);

		List <UserWallet> updatedWallet  = new ArrayList<UserWallet>();


		//only applicable for buy transaction, if user does not have this currency and buy this , initialize and insert
		if(walletA.stream().noneMatch(w -> w.getCryptocurrency().equals(toCurrency)))
		{
			UserWallet newWallet = new UserWallet();
			newWallet.setUserId(userId);
			newWallet.setCryptocurrency(toCurrency);
			// For CONST_BUY transactions, initialize with purchased quantity
			newWallet.setBalance(convertedAmount);

			updatedWallet.add(newWallet);
		}

		
		logger.info(" fromCurrency {}, toCurrency {}  " , fromCurrency, toCurrency);
		logger.info(" amountToTradeAtCurrentPrice {}, convertedAmount {}  " , amountToTradeAtCurrentPrice, convertedAmount);
		logger.info(" tradeType  {}  " , tradeType);
		
		
		//update USDT balance
		for(UserWallet uwall : walletA) {

			/*
			 * if (uwall.getCryptocurrency().equals(CONST_USDT)) { //for BUY : USDT bal will
			 * reduce ( transaction Amount will be passed in as negative value) //for SELL :
			 * USDT bal will increase ( transaction Amount will be passed in as postive
			 * value) uwall.setBalance(uwall.getBalance() + amountToTradeAtCurrentPrice); }
			 * else
			 */
			
			if(uwall.getCryptocurrency().equals(fromCurrency)) 
			{
				//for BUY : crypto  will increase ( quantity will be passed in as +ve value)
				//for SELL : crypto will reduce (  quantity will be passed in as -ve value)

				uwall.setBalance(uwall.getBalance() + amountToTradeAtCurrentPrice);
			}
			else if(uwall.getCryptocurrency().equals(toCurrency)) 
			{
				//for BUY : crypto  will increase ( quantity will be passed in as +ve value)
				//for SELL : crypto will reduce (  quantity will be passed in as -ve value)
				
				uwall.setBalance(uwall.getBalance() + convertedAmount);
			}

			updatedWallet.add(uwall);

		}

		userWalletRepository.saveAll(updatedWallet);

		logger.info("9. updateWallet ");

	}



}
