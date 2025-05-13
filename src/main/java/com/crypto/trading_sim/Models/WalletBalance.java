package com.crypto.trading_sim.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalance {
    private Long userId;
    private String coinSymbol;
    private BigDecimal quantity;
}
