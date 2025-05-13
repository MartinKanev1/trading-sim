package com.crypto.trading_sim.Controllers;

import com.crypto.trading_sim.DTOs.CryptoHoldingDTO;
import com.crypto.trading_sim.Models.WalletBalance;
import com.crypto.trading_sim.Services.WalletBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletBalanceController {

    private final WalletBalanceService walletBalanceService;

    public WalletBalanceController(WalletBalanceService walletBalanceService) {
        this.walletBalanceService = walletBalanceService;
    }

    @GetMapping("/{userId}/holdings")
    public ResponseEntity<List<CryptoHoldingDTO>> getUserHoldings(@PathVariable Long userId) {
        List<CryptoHoldingDTO> holdings = walletBalanceService.getHoldingsForUser(userId);
        return ResponseEntity.ok(holdings);
    }
}

