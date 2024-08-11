package com.example.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.service.PriceService;


@Component
public class PriceScheduler {
	@Autowired 
	private PriceService priceService;

	@Transactional
	@Scheduled(fixedRate = 10000) 
	public void fetchAndUpdatePrices() {
		
		priceService.updateBestPrices(); }

}
