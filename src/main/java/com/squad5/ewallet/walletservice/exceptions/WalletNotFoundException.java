package com.squad5.ewallet.walletservice.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String msg) { super(msg); }
}
