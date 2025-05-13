package com.crypto.trading_sim.DTOs;

import java.math.BigDecimal;
import java.util.List;

public record CryptocurrencyDTO(
        String symbol,
        String name,
        int rank,
        String logoUrl,
        String krakenPair,
        BigDecimal currentPrice,
        List<PriceSnapshotDTO> snapshots
) {
}
