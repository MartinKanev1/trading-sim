package com.crypto.trading_sim.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Long userId;
    private String coinSymbol;
    private TransactionType type;
    private BigDecimal quantity;
    private BigDecimal priceAtTime;
    private BigDecimal totalValue;
    private LocalDateTime timestamp;
}
