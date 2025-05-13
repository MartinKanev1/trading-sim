package com.crypto.trading_sim.DTOs;

import java.math.BigDecimal;

public record WalletBalanceDTO(
        Long userId,
        String coinSymbol,
        BigDecimal quantity
) {
}
