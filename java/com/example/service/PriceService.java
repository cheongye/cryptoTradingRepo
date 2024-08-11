package com.example.service;

import com.example.dto.PriceDataDTO;
import com.example.model.TradingPair;
import com.example.repository.TradingPairRepository;
import com.example.util.CryptoApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PriceService {

	private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

	@Autowired
	private TradingPairRepository tradingPairRepository;

	@Autowired
	private CryptoApiProperties cryptoApiProperties;

	private final RestTemplate restTemplate = new RestTemplate();
	private final String CONST_BINANCE = "BINANCE";
	private final String CONST_HUOBI = "HUOBI";


	private List<String> supportedSymbols ;


	@PostConstruct
	public void init() {
		this.supportedSymbols = cryptoApiProperties.getSupportedSymbols();
	}


	public Map<String, Map<String, PriceDataDTO>> updatePricesFromApiBinance(String url, String source) {


		Map<String, Map<String, PriceDataDTO>> priceDataMap = new HashMap<>();
		try {

			String symbolList = supportedSymbols.stream()
					.map(s -> "\"" + s + "\"")
					.collect(Collectors.joining(",", "[", "]"));

			StringBuffer sbUrl = new StringBuffer(url).append("?symbols=").append(symbolList);

			String responseData = restTemplate.getForObject(sbUrl.toString(), String.class);
			JSONArray tickers;

			tickers = new JSONArray(responseData); // Adjust for Binance format

			for (int i = 0; i < tickers.length(); i++) {
				JSONObject ticker = tickers.getJSONObject(i);
				String symbol = ticker.getString("symbol").toUpperCase();

				if (supportedSymbols.contains(symbol)) {
					double bidPrice = ticker.getDouble("bidPrice");
					double askPrice = ticker.getDouble("askPrice");


					double bidQuantity = ticker.getDouble("bidQty");
					double askQuantity = ticker.getDouble("askQty");

					// Create or update PriceData in the unified map
					PriceDataDTO priceData = new PriceDataDTO(bidPrice, bidQuantity, askPrice, askQuantity, source,source);
					priceDataMap
					.computeIfAbsent(source, k -> new HashMap<>())
					.put(symbol, priceData);

					// Log the update to debug
					logger.info("Updated {} with source {}: Bid - {}, BidQty - {}, Ask - {}, askQuantity - {}", symbol, source, bidPrice, bidQuantity, askPrice,askQuantity );
				}
			}

			calculateAndIncludeConversionRates(source, priceDataMap);

		} catch (Exception e) {
			logger.error("Error updating prices from " + source, e);
		} finally {
			return priceDataMap; 
		}
	}


	public Map<String, Map<String, PriceDataDTO>>  updatePricesFromApiHuobi(String url, String source) {

		Map<String, Map<String, PriceDataDTO>> priceDataMap = new HashMap<>();
		try {

			String responseData = restTemplate.getForObject(url, String.class);
			JSONArray tickers;

			JSONObject jsonObject = new JSONObject(responseData) ;

			tickers = jsonObject.getJSONArray("data");

			for (int i = 0; i < tickers.length(); i++) {
				JSONObject ticker = tickers.getJSONObject(i);
				String symbol = ticker.getString("symbol").toUpperCase();

				if (supportedSymbols.contains(symbol)) {
					double bidPrice = ticker.getDouble("bid");
					double askPrice = ticker.getDouble("ask");
					double bidQuantity = ticker.getDouble("bidSize");
					double askQuantity = ticker.getDouble("askSize");

					// Create or update PriceData in the unified map
					PriceDataDTO priceData = new PriceDataDTO(bidPrice, bidQuantity, askPrice, askQuantity, source, source);
					priceDataMap
					.computeIfAbsent(source, k -> new HashMap<>())
					.put(symbol, priceData);

					// Log the update to debug
					logger.info("Updated {} with source {}: Bid - {}, BidQty - {}, Ask - {}, askQuantity - {}", symbol, source, bidPrice, bidQuantity, askPrice,askQuantity );
				}
			}

			// Calculate and include BTCETH and ETHBTC prices
			calculateAndIncludeConversionRates(source, priceDataMap);

		} catch (Exception e) {
			logger.error("Error updating prices from " + source, e);
		}
		finally {
			return priceDataMap;
		}
	}



	private void calculateAndIncludeConversionRates(String source,
			Map<String, Map<String, PriceDataDTO>> priceDataMap) {

		Map<String, PriceDataDTO> sourceData = priceDataMap.get(source);

		// Calculate BTCETH
		if (sourceData.containsKey("BTCUSDT") && sourceData.containsKey("ETHUSDT")) {
			PriceDataDTO btcUsdt = sourceData.get("BTCUSDT");
			PriceDataDTO ethUsdt = sourceData.get("ETHUSDT");

			double btcEthBidPrice = btcUsdt.getBidPrice() / ethUsdt.getAskPrice();
			double btcEthAskPrice = btcUsdt.getAskPrice() / ethUsdt.getBidPrice();
			double btcEthBidQuantity = Math.min(btcUsdt.getBidQuantity(), ethUsdt.getBidQuantity() / btcEthBidPrice);
			double btcEthAskQuantity = Math.min(btcUsdt.getAskQuantity() / btcEthAskPrice, ethUsdt.getAskQuantity());

			
			// Store the BTCETH conversion
			PriceDataDTO btcEthPriceData = new PriceDataDTO(btcEthBidPrice, btcEthBidQuantity, btcEthAskPrice, btcEthAskQuantity, source, source);
			sourceData.put("BTCETH", btcEthPriceData);

			// Log the BTCETH conversion rate
			logger.info("Calculated BTCETH: Bid - {}, BidQty - {}, Ask - {}, AskQty - {}",
					btcEthBidPrice, btcEthBidQuantity, btcEthAskPrice, btcEthAskQuantity);
		}

		// Calculate ETHBTC
		if (sourceData.containsKey("ETHUSDT") && sourceData.containsKey("BTCUSDT")) {
			PriceDataDTO ethUsdt = sourceData.get("ETHUSDT");
			PriceDataDTO btcUsdt = sourceData.get("BTCUSDT");

			double ethBtcBidPrice = ethUsdt.getBidPrice() / btcUsdt.getAskPrice();
			double ethBtcAskPrice = ethUsdt.getAskPrice() / btcUsdt.getBidPrice();

			double ethBtcBidQuantity = Math.min(ethUsdt.getBidQuantity() / ethBtcBidPrice, btcUsdt.getBidQuantity());
			double ethBtcAskQuantity = Math.min(btcUsdt.getAskQuantity() / ethBtcAskPrice, ethUsdt.getAskQuantity());



			// Store the ETHBTC conversion
			PriceDataDTO ethBtcPriceData = new PriceDataDTO(ethBtcBidPrice, ethBtcBidQuantity, ethBtcAskPrice, ethBtcAskQuantity, source, source);
			sourceData.put("ETHBTC", ethBtcPriceData);

			// Log the ETHBTC conversion rate
			logger.info("Calculated ETHBTC: Bid - {}, BidQty - {}, Ask - {}, AskQty - {}",
					ethBtcBidPrice, ethBtcBidQuantity, ethBtcAskPrice, ethBtcAskQuantity);
		}		
	}


	public List<TradingPair> getBestPrices() {

		logger.info("get best pricess ....");

		List<TradingPair> tradingPairs = new ArrayList<>();
		Map<String, Map<String, PriceDataDTO>> priceDataMap = new HashMap<>();

		try {
			// Iterate over configured sources
			for (Map.Entry<String, String> entry : cryptoApiProperties.getSources().entrySet()) {
				String sourceName = entry.getKey();
				String url = entry.getValue();

				switch (sourceName) {
				case CONST_HUOBI:
					priceDataMap.putAll(updatePricesFromApiHuobi(url, sourceName));
					break;
				case CONST_BINANCE:
					priceDataMap.putAll(updatePricesFromApiBinance(url, sourceName));
					break;
				default:
					logger.error("invalid source");
					break;
				}
			}

			Map<String, PriceDataDTO> bestPrices = new HashMap<>();

			for (Map<String, PriceDataDTO> prices : priceDataMap.values()) {
				for (Map.Entry<String, PriceDataDTO> entry : prices.entrySet()) {
					String symbol = entry.getKey();
					PriceDataDTO priceDataDTO = entry.getValue();

					// If the symbol is not yet in bestPrices, add it
					if (!bestPrices.containsKey(symbol)) {
						bestPrices.put(symbol, priceDataDTO);
					} else {
						PriceDataDTO currentBest = bestPrices.get(symbol);

						// Update bid price if current price is higher
						if (priceDataDTO.getBidPrice() > currentBest.getBidPrice()) {
							currentBest.setBidPrice(priceDataDTO.getBidPrice());
							currentBest.setBidQuantity(priceDataDTO.getBidQuantity());
							currentBest.setBidSource(priceDataDTO.getBidSource());
						}

						// Update ask price if current price is lower
						if (priceDataDTO.getAskPrice() < currentBest.getAskPrice()) {
							currentBest.setAskPrice(priceDataDTO.getAskPrice());
							currentBest.setAskQuantity(priceDataDTO.getAskQuantity());
							currentBest.setAskSource(priceDataDTO.getAskSource());
						}

						bestPrices.put(symbol, currentBest);

					}
				}

			}

			for (Map.Entry<String, PriceDataDTO> entry : bestPrices.entrySet()) {

				String symbol = entry.getKey();

				PriceDataDTO priceData = entry.getValue();


				double bestBidPrice = priceData.getBidPrice();

				double bestBidQuantity = priceData.getBidQuantity();

				String bestBidSource = priceData.getBidSource();

				double bestAskPrice = priceData.getAskPrice();

				double bestAskQuantity = priceData.getAskQuantity();

				String bestAskSource = priceData.getAskSource();

				TradingPair tradingPair = new TradingPair(symbol,bestBidPrice,bestBidQuantity,bestBidSource, bestAskPrice, bestAskQuantity, bestAskSource,LocalDateTime.now());

				tradingPairs.add(tradingPair);

			}



			// Clear temporary storage after saving
			priceDataMap.clear();
		} catch (Exception e) {
			logger.error("Error updating best prices", e);
		}
		finally {
			return tradingPairs;
		}
	}


	public void updateBestPrices() {
		logger.info("0. upding best pricess ...."); 

		List<TradingPair> tradingPairList = getBestPrices();

		for (TradingPair tradingPair : tradingPairList) {
			tradingPairRepository.save(tradingPair);
		}

		logger.info("9. upding best pricess ...."); 
	} 
}
