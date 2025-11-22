package com.squad5.ewallet.walletservice.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String msg) { super(msg); }
}
