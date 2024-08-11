package com.example.repository;

import  com.example.model.Trade;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, String> {

	List<Trade> findByUserId(String userId);
	List<Trade> findByTradeRequestId(UUID tradeRequestId);
	
}
