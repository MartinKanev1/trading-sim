package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.CryptoHoldingDTO;
import com.crypto.trading_sim.Models.WalletBalance;

import java.util.List;

public interface WalletBalanceService {



    List<CryptoHoldingDTO> getHoldingsForUser(Long userId);
}
