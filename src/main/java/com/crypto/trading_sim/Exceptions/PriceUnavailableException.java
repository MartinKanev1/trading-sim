package com.crypto.trading_sim.Exceptions;

public class PriceUnavailableException extends RuntimeException {
    public PriceUnavailableException(String message) {
        super(message);
    }
}
