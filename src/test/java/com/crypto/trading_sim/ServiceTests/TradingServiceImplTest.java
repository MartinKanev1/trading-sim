package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.Exceptions.*;
import com.crypto.trading_sim.Models.*;
import com.crypto.trading_sim.Repositories.*;
import com.crypto.trading_sim.Services.TradingServiceImpl;
import com.crypto.trading_sim.Services.KrakenWebSocketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradingServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private CryptocurrencyRepository cryptocurrencyRepository;
    @Mock private KrakenWebSocketService krakenWebSocketService;
    @Mock private WalletBalanceRepository walletBalanceRepository;
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks private TradingServiceImpl tradingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuyCrypto_Successful() {
        Long userId = 1L;
        String symbol = "BTC";
        BigDecimal quantityToBuy = new BigDecimal("0.01");
        BigDecimal currentPrice = new BigDecimal("50000.00");
        BigDecimal userBalance = new BigDecimal("1000.00");

        User user = new User();
        user.setId(userId);
        user.setBalanceUsd(userBalance);

        Cryptocurrency coin = new Cryptocurrency(symbol, "Bitcoin", 1, "logo", "BTC/USD");

        when(cryptocurrencyRepository.findBySymbol(symbol)).thenReturn(Optional.of(coin));
        when(krakenWebSocketService.getLatestPrice("BTC/USD")).thenReturn(currentPrice);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletBalanceRepository.getHoldings(userId, symbol)).thenReturn(null); // New holding

        tradingService.buyCrypto(userId, symbol, quantityToBuy);

        verify(userRepository).updateBalance(any(User.class));
        verify(walletBalanceRepository).insertHolding(userId, symbol, quantityToBuy);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testBuyCrypto_InsufficientBalance() {
        Long userId = 1L;
        String symbol = "BTC";
        BigDecimal quantityToBuy = new BigDecimal("1.0");
        BigDecimal currentPrice = new BigDecimal("50000.00");
        BigDecimal userBalance = new BigDecimal("1000.00");

        User user = new User();
        user.setId(userId);
        user.setBalanceUsd(userBalance);

        Cryptocurrency coin = new Cryptocurrency(symbol, "Bitcoin", 1, "logo", "BTC/USD");

        when(cryptocurrencyRepository.findBySymbol(symbol)).thenReturn(Optional.of(coin));
        when(krakenWebSocketService.getLatestPrice("BTC/USD")).thenReturn(currentPrice);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(InsufficientBalanceException.class, () ->
                tradingService.buyCrypto(userId, symbol, quantityToBuy)
        );
    }

    @Test
    void testSellCrypto_Successful() {
        Long userId = 1L;
        String symbol = "BTC";
        BigDecimal quantityToSell = new BigDecimal("0.01");
        BigDecimal currentPrice = new BigDecimal("50000.00");
        BigDecimal holdingQty = new BigDecimal("0.02");

        Cryptocurrency coin = new Cryptocurrency(symbol, "Bitcoin", 1, "logo", "BTC/USD");
        User user = new User();
        user.setId(userId);
        user.setBalanceUsd(new BigDecimal("1000.00"));

        when(cryptocurrencyRepository.findBySymbol(symbol)).thenReturn(Optional.of(coin));
        when(krakenWebSocketService.getLatestPrice("BTC/USD")).thenReturn(currentPrice);
        when(walletBalanceRepository.getHoldings(userId, symbol)).thenReturn(
                new WalletBalance(userId, symbol, holdingQty)
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        tradingService.sellCrypto(userId, symbol, quantityToSell);

        verify(walletBalanceRepository).updateQuantity(userId, symbol, new BigDecimal("0.01"));
        verify(userRepository).updateBalance(any(User.class));
        verify(transactionRepository).save(any(Transaction.class));
    }
}

