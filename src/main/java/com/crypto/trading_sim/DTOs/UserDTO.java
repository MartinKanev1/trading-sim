package com.crypto.trading_sim.DTOs;

import java.math.BigDecimal;

public record UserDTO(
        Long id,
        String email,
        String password,
        BigDecimal balanceUsd
) {
}
