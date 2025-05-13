package com.crypto.trading_sim.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceSnapshot {
    private Long id;
    private String coinSymbol;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
