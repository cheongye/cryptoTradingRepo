package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.TradeRequestDTO;
import com.example.dto.UserWalletDTO;
import com.example.model.TradingPair;
import com.example.service.PriceService;
import com.example.service.TradeService;
import com.example.service.TradingHistoryService;
import com.example.service.UserWalletService;

@RestController
@RequestMapping("/api")
public class TradingController {
	

    @Autowired
    private UserWalletService userWalletService;
    
    
    @Autowired
    private PriceService priceService;
    
    @Autowired
    private TradeService tradeService;
    
    @Autowired
    private TradingHistoryService tradingHistoryService;
    

    @Value("${wallet.TestId}")
    private String testId ;
    
	private static final Logger logger = LoggerFactory.getLogger(TradingController.class);
	

    //Task 4
    
	 //sample
		/*
		 * curl -X GET http://localhost:8080/api/wallet
		 */		
    @GetMapping("/wallet")
    public ResponseEntity<List<UserWalletDTO>> getWallet() {
    	
    	logger.info("inside wallet API" );
    	List<UserWalletDTO> walletDto= userWalletService.getWalletBalance(testId);

    	
    	logger.info("wallet Dto info {} " , walletDto );
        return ResponseEntity.ok(walletDto);
    }
    
    
    //Task 2
	/*
	 * curl -X GET http://localhost:8080/api/price
	 */
    
    @GetMapping("/price")
    public ResponseEntity<List<TradingPair>> getBestAggregatedPrice() {
    	List<TradingPair> tradingPairs = priceService.getBestPrices(); 

        if (tradingPairs == null || tradingPairs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
    	return ResponseEntity.ok(tradingPairs);
    }
    
    
    

    //Task 3
    
	/*
	 * curl -X POST http://localhost:8080/api/trade ^ -H
	 * "Content-Type: application/json" ^ -d
	 * "{\"fromCurrency\": \"USDT\", \"toCurrency\": \"BTC\", \"fromAmountToTrade\": 5000, \"tradeType\": \"BUY\"}"
	 * 
	 * 
	 * curl -X POST http://localhost:8080/api/trade ^ -H
	 * "Content-Type: application/json" ^ -d
	 * "{\"fromCurrency\": \"BTC\", \"toCurrency\": \"ETH\", \"fromAmountToTrade\": 0.001, \"tradeType\": \"BUY\"}"
	 * 
	 * 
	 * 
	 * curl -X POST http://localhost:8080/api/trade ^ -H
	 * "Content-Type: application/json" ^ -d
	 * "{\"fromCurrency\": \"USDT\", \"toCurrency\": \"ETH\", \"fromAmountToTrade\": 75000, \"tradeType\": \"BUY\"}"
	 * 
	 * 
	 * 
	 * curl -X POST http://localhost:8080/api/trade ^ -H
	 * "Content-Type: application/json" ^ -d
	 * "{\"fromCurrency\": \"USDT\", \"toCurrency\": \"BTC\", \"fromAmountToTrade\": 7500, \"tradeType\": \"BUY\"}"
	 * 
	 */
    
    @PostMapping("/trade")
    public ResponseEntity<String> trade(@RequestBody TradeRequestDTO tradeRequest) {
        String response = tradeService.
                executeTrade(tradeRequest.getFromCurrency(), tradeRequest.getToCurrency(), tradeRequest.getFromAmountToTrade(), tradeRequest.getTradeType(), testId) ;
        
        return ResponseEntity.ok(response);
    }
    
    
    
    
    //Task 5
    
	/*
	 * curl -X GET http://localhost:8080/api/trading-history
	 */    
    
    @GetMapping("/trading-history")
	public ResponseEntity<Map<String, Object>> getTradingHistory() {
		List<Map<String, Object>> tradeList = tradingHistoryService.getTradesByUserId(testId);
	    
	    if (tradeList.isEmpty()) {
			throw new IllegalArgumentException("No trading history at the moment, please trade.");
	    }
	
	    Map<String, Object> response = new HashMap<>();
	
	    response.put("trades", tradeList);
	    return ResponseEntity.ok(response);
	}



	@GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful");
    }
    
    

}
