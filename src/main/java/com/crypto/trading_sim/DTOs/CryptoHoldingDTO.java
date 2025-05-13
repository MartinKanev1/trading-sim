package com.crypto.trading_sim.DTOs;

import java.math.BigDecimal;

public record CryptoHoldingDTO(
        String symbol,
        String name,
        String logoUrl,
        BigDecimal currentPrice,
        BigDecimal quantity
) {
}
