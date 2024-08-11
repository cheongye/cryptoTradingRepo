package com.example.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "crypto.api")
public class CryptoApiProperties {

    private Map<String, String> sources;
    

    @Value("${crypto.supported.symbols}")
    private  String supportedSymbols;
     
    
    @Value("${crypto.symbol.USDT}")
    private  String constantUSDT;
    
    @Value("${crypto.symbol.currency}")
    private  String allowedSymbols;

    //supportedSymbols for trade
    public List<String> getSupportedSymbols() {
        return Arrays.asList(supportedSymbols.split(","));
    }
    

    
    public void setAllowedSymbols(String allowedSymbols) {
    	this.allowedSymbols = allowedSymbols;
    }
    public List<String> getAllowedSymbols() {
    	return Arrays.asList(allowedSymbols.split(","));
    }
    
    public Map<String, String> getSources() {
        return sources;
    }

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }

	public String getConstantUSDT() {
		return constantUSDT;
	}

	public void setConstantUSDT(String constantUSDT) {
		this.constantUSDT = constantUSDT;
	}

}
