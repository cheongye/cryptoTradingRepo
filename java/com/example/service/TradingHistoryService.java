package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.TradeHistoryDTO;
import com.example.model.Trade;
import com.example.model.TradeRequest;
import com.example.repository.TradeRepository;
import com.example.repository.TradeRequestRepository;

@Service
@Transactional
public class TradingHistoryService {

	@Autowired
	private TradeRepository tradeRepository;

	@Autowired
	private TradeRequestRepository tradeRequestRepository;

	public List<Map<String, Object>> getTradesByUserId(String userId) {

		List<TradeRequest> tradeRequestList = tradeRequestRepository.findByUserId(userId);


		if(tradeRequestList.isEmpty()) {
			return new ArrayList<>();
		}


		List<Map<String, Object>> tradeList = new ArrayList<>();

		/*
		 * { "TRADE_TYPE": "BUY", "USER_ID": "userOne", "REQUESTED_TRADE_DATE_TIME":
		 * "2024-08-11T17:40:05.06616", "TRADE_REQUEST_ID":
		 * "232250b1-00b0-42f7-a357-a2afe8bfd489", "FROM_AMT": 5000, "FROM_CURRENCY":
		 * "USDT", "TO_CURRENCY": "BTC" "trades": [ { "TOTAL_VALUE": 0.03108,
		 * "FROM_AMOUNT": 1902.1584708, "ACTUAL_TRADE_DATE_TIME":
		 * "2024-08-11T17:40:05.2643", "TRADE_ID":
		 * "567d084e-7ca7-48ae-8a93-64c7df4421af" }, { "TOTAL_VALUE":
		 * 0.05061666323050501, "FROM_AMOUNT": 3097.8415292, "ACTUAL_TRADE_DATE_TIME":
		 * "2024-08-11T17:40:05.438084", "TRADE_ID":
		 * "1dac5139-e37d-48c1-8de9-a019080cfdf9" } ] }
		 */
		for (TradeRequest tradeReq : tradeRequestList) {

			Map<String, Object> tradeData = new HashMap<>();

			tradeData.put("TRADE_REQUEST_ID", tradeReq.getTradeRequestId());
			tradeData.put("TRADE_TYPE", tradeReq.getTradeType());
			tradeData.put("USER_ID", tradeReq.getUserId());
			tradeData.put("REQUESTED_TRADE_DATE_TIME", tradeReq.getRequestedTradeDateTime());
			tradeData.put("ACTUAL_TRADE_DATE_TIME", tradeReq.getActualTradeDateTime());
			tradeData.put("FROM_AMOUNT", tradeReq.getFromAmt());
			tradeData.put("FROM_CURRENCY", tradeReq.getFromCurrency());
			tradeData.put("TO_CURRENCY", tradeReq.getToCurrency());

			List<Trade> userTradeList = tradeRepository.findByTradeRequestId(tradeReq.getTradeRequestId());

			List<TradeHistoryDTO> tradeDetails = userTradeList.stream().map(trade -> {
				TradeHistoryDTO detail = new TradeHistoryDTO();
				detail.setTotalValue(trade.getTotalValue());
				detail.setFromAmount(trade.getFromAmount());
				detail.setActualTradeDateTime(trade.getActualTradeDateTime());
				detail.setTradeId(trade.getTradeId());
				return detail;
			}).collect(Collectors.toList());

			tradeData.put("TRADE_DETAILS", tradeDetails);

			tradeList.add(tradeData);
		}
		

		return tradeList;


	}

}
