package com.crypto.trading_sim.Models;

import lombok.*;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String password;
    private BigDecimal balanceUsd;
}
