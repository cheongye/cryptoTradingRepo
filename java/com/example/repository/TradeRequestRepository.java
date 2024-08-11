package com.example.repository;

import  com.example.model.TradeRequest;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, String> {

	TradeRequest findByTradeRequestId(UUID uuid);

	List<TradeRequest> findByUserId(String userId);

}
