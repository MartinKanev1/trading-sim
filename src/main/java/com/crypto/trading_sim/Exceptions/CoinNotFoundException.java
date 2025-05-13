package com.crypto.trading_sim.Exceptions;

public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(String message) {
        super(message);
    }
}
