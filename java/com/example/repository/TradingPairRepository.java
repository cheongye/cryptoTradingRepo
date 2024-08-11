package com.example.repository;

import java.util.Optional;
import  com.example.model.TradingPair;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingPairRepository extends JpaRepository<TradingPair, String> {
    Optional<TradingPair> findBySymbol(String symbol);
}
