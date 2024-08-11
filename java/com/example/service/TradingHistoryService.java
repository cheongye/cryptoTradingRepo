package com.example.service;

import com.example.dto.UserWalletDTO;
import com.example.model.Trade;
import com.example.model.TradeRequest;
import com.example.model.UserWallet;
import com.example.repository.TradeRepository;
import com.example.repository.TradeRequestRepository;
import com.example.repository.UserWalletRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TradingHistoryService {
    
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeRequestRepository tradeRequestRepository;

    public List<Map<String, Object>> getTradesByUserId(String userId) {
    	List<Trade> userTrade = tradeRepository.findByUserId(userId);
        
    	
    	if(userTrade.isEmpty()) {
            return new ArrayList<>();
    	}
    	
        List<Map<String, Object>> tradeList = new ArrayList<>();
        for (Trade trade : userTrade) {
            Map<String, Object> tradeData = new HashMap<>();
            tradeData.put("TRADE_ID", trade.getTradeId());
            tradeData.put("ACTUAL_TRADE_DATE_TIME", trade.getActualTradeDateTime());
            tradeData.put("FROM_AMOUNT", trade.getFromAmount());
            tradeData.put("FROM_CURRENCY", trade.getFromCurrency());
            tradeData.put("TO_CURRENCY", trade.getToCurrency());
            tradeData.put("TOTAL_VALUE", trade.getTotalValue());
            tradeData.put("TRADE_REQUEST_ID", trade.getTradeRequestId());
            tradeData.put("TRADE_TYPE", trade.getTradeType());
            tradeData.put("USER_ID", trade.getUserId());
            
            // Add related trade request info
            TradeRequest tradeRequest = tradeRequestRepository.findByTradeRequestId(trade.getTradeRequestId());
            if (tradeRequest != null) {
                tradeData.put("REQUESTED_TRADE_DATE_TIME", tradeRequest.getRequestedTradeDateTime());
                tradeData.put("FROM_AMT", tradeRequest.getFromAmt());
                tradeData.put("TO_CURRENCY", tradeRequest.getToCurrency());
            }
            
            tradeList.add(tradeData);
        }

        return tradeList;
        
        
    }

}
