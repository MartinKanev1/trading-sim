package com.crypto.trading_sim.DTOs;

import com.crypto.trading_sim.Models.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        Long userId,
        String coinSymbol,
        TransactionType type,
        BigDecimal quantity,
        BigDecimal priceAtTime,
        BigDecimal totalValue,
        LocalDateTime timestamp,
        BigDecimal profitLoss
) {
}
