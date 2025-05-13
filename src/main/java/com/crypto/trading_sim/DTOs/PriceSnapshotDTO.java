package com.crypto.trading_sim.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceSnapshotDTO(
        Long id,
        String coinSymbol,
        BigDecimal price,
        LocalDateTime timestamp
) {
}
