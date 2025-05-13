package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.TransactionDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getTransactionsForUser(Long userId);
}
