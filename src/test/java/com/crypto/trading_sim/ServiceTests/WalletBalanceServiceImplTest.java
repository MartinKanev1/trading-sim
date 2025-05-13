package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.DTOs.CryptoHoldingDTO;
import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.Models.WalletBalance;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import com.crypto.trading_sim.Services.CryptocurrencyService;
import com.crypto.trading_sim.Services.WalletBalanceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletBalanceServiceImplTest {

    @Mock private WalletBalanceRepository walletBalanceRepository;
    @Mock private CryptocurrencyService cryptocurrencyService;

    @InjectMocks private WalletBalanceServiceImpl walletBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHoldingsForUser_ReturnsHoldings() {
        Long userId = 1L;
        WalletBalance btcHolding = new WalletBalance(userId, "BTC", new BigDecimal("0.01"));

        CryptocurrencyDTO btcDto = new CryptocurrencyDTO(
                "BTC", "Bitcoin", 1, "logo_url", "BTC/USD", new BigDecimal("50000.00"), List.of()
        );

        when(walletBalanceRepository.getCryptoHoldingsForUser(userId)).thenReturn(List.of(btcHolding));
        when(cryptocurrencyService.getBySymbol("BTC")).thenReturn(Optional.of(btcDto));

        List<CryptoHoldingDTO> result = walletBalanceService.getHoldingsForUser(userId);

        assertEquals(1, result.size());
        CryptoHoldingDTO holding = result.get(0);
        assertEquals("BTC", holding.symbol());
        assertEquals(new BigDecimal("0.01"), holding.quantity());
        assertEquals(new BigDecimal("50000.00"), holding.currentPrice());
    }

    @Test
    void testGetHoldingsForUser_FiltersUnknownSymbols() {
        Long userId = 1L;
        WalletBalance unknownHolding = new WalletBalance(userId, "XYZ", new BigDecimal("100"));

        when(walletBalanceRepository.getCryptoHoldingsForUser(userId)).thenReturn(List.of(unknownHolding));
        when(cryptocurrencyService.getBySymbol("XYZ")).thenReturn(Optional.empty());

        List<CryptoHoldingDTO> result = walletBalanceService.getHoldingsForUser(userId);

        assertTrue(result.isEmpty());  // Unknown symbol should be filtered out
    }
}

