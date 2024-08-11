package com.example.repository;

import com.example.model.UserWallet;
import com.example.model.UserWalletId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, UserWalletId> {

    List<UserWallet> findByUserId(String userId);
    
    // Find by composite key
    Optional<UserWallet> findByUserIdAndCryptocurrency(String userId, String cryptocurrency);


}
