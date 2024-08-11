package com.example.service;

import com.example.dto.UserWalletDTO;
import com.example.model.UserWallet;
import com.example.repository.UserWalletRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserWalletService {

	private final UserWalletRepository userWalletRepository;

	public UserWalletService(UserWalletRepository userWalletRepository) {
		this.userWalletRepository = userWalletRepository;
	}

	public List<UserWalletDTO> getWalletBalance(String id) {
		List<UserWallet> walletList = userWalletRepository.findByUserId(id);
		
		 // Check if any wallets were found
        if (walletList.isEmpty()) {
            throw new RuntimeException("Wallet not found for user ID: " + id);
        }
		
		
        // Convert UserWallet entities to UserWalletDTOs
        return walletList.stream()
                .map(wallet -> new UserWalletDTO(wallet.getUserId(), wallet.getCryptocurrency(), wallet.getBalance()))
                .collect(Collectors.toList());
        
	}

}

