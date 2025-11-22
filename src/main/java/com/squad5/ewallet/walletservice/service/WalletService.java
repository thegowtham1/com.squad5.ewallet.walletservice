package com.squad5.ewallet.walletservice.service;

import com.squad5.ewallet.walletservice.entity.Wallet;
import com.squad5.ewallet.walletservice.exceptions.InsufficientFundsException;
import com.squad5.ewallet.walletservice.exceptions.WalletNotFoundException;
import com.squad5.ewallet.walletservice.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final Random random = new Random();

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Create demo wallets for given userIds. If a wallet already exists for a user, it's skipped.
     */
    @Transactional
    public void createDemoWalletsForUsers(List<Long> userIds, String currency, String createdBy) {
        for (Long userId : userIds) {
            Optional<Wallet> exists = walletRepository.findByUserIdAndIsDeleted(userId, (byte)0);
            if (exists.isPresent()) continue;

            Wallet w = new Wallet();
            w.setUserId(userId);
            w.setAccountNumber(generateAccountNumber(userId));
            w.setBalance(BigDecimal.ZERO);
            w.setCurrency(currency != null ? currency : "INR");
            w.setCreatedBy(createdBy);
            walletRepository.save(w);
        }
    }

    private String generateAccountNumber(Long userId) {
        // simple generator: ACC + userId + random 4 digits
        String candidate;
        do {
            int r = 1000 + random.nextInt(9000);
            candidate = "ACC" + userId + r;
        } while (walletRepository.existsByAccountNumber(candidate));
        return candidate;
    }

    /**
     * Get balance for userId
     */
    public BigDecimal getBalanceForUser(Long userId) {
        Wallet w = walletRepository.findByUserIdAndIsDeleted(userId, (byte)0)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId: " + userId));
        return w.getBalance();
    }

    /**
     * Credit amount to user's wallet (safe with row lock).
     */
    @Transactional
    public BigDecimal credit(Long userId, BigDecimal amount, String updatedBy) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        Wallet w = walletRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId: " + userId));
        BigDecimal newBalance = w.getBalance().add(amount);
        w.setBalance(newBalance);
        w.setUpdatedBy(updatedBy);
        walletRepository.save(w);
        return newBalance;
    }

    /**
     * Debit amount from user's wallet (safe with row lock).
     */
    @Transactional
    public BigDecimal debit(Long userId, BigDecimal amount, String updatedBy) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        Wallet w = walletRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId: " + userId));
        BigDecimal current = w.getBalance();
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for userId: " + userId);
        }
        BigDecimal newBalance = current.subtract(amount);
        w.setBalance(newBalance);
        w.setUpdatedBy(updatedBy);
        walletRepository.save(w);
        return newBalance;
    }

    //to fetch all the required user details
    public Wallet getWalletDetails(Long userId) {
        return walletRepository.findByUserIdAndIsDeleted(userId, (byte)0)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for userId: " + userId));
    }

}
