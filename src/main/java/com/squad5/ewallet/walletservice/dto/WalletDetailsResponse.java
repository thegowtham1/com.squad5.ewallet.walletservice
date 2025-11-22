package com.squad5.ewallet.walletservice.dto;

import java.math.BigDecimal;

public class WalletDetailsResponse {

    private Long userId;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;

    public WalletDetailsResponse(Long userId, String accountNumber, BigDecimal balance, String currency) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
}

