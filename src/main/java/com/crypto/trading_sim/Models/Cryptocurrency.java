package com.crypto.trading_sim.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {
    private String symbol;
    private String name;
    private int rank;
    private String logoUrl;
    private String krakenPair;
}
