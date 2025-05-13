package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.Exceptions.*;
import com.crypto.trading_sim.Models.*;
import com.crypto.trading_sim.Repositories.CryptocurrencyRepository;
import com.crypto.trading_sim.Repositories.TransactionRepository;
import com.crypto.trading_sim.Repositories.UserRepository;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TradingServiceImpl implements TradingService {

    private final WalletBalanceRepository walletBalanceRepository;
    @Autowired
    private final UserRepository userRepository;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final KrakenWebSocketService krakenWebSocketService;
    private final TransactionRepository transactionRepository;

    public TradingServiceImpl(
            WalletBalanceRepository walletBalanceRepository,
            UserRepository userRepository,
            CryptocurrencyRepository cryptocurrencyRepository,
            KrakenWebSocketService krakenWebSocketService,
            TransactionRepository transactionRepository
    ) {
        this.walletBalanceRepository = walletBalanceRepository;
        this.userRepository = userRepository;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.krakenWebSocketService = krakenWebSocketService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void buyCrypto(Long userId, String coinSymbol, BigDecimal quantityToBuy) {


        Cryptocurrency coin = cryptocurrencyRepository.findBySymbol(coinSymbol)
                .orElseThrow(() -> new CoinNotFoundException("Coin not found: " + coinSymbol));

        String krakenPair = coin.getKrakenPair();
        BigDecimal currentPrice = krakenWebSocketService.getLatestPrice(krakenPair);

        if (currentPrice == null) {
            throw new PriceUnavailableException("Current price for " + coinSymbol + " not available.");
        }


        BigDecimal totalCost = currentPrice.multiply(quantityToBuy);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));


        if (user.getBalanceUsd().compareTo(totalCost) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Required: " + totalCost + ", Available: " + user.getBalanceUsd());
        }


        user.setBalanceUsd(user.getBalanceUsd().subtract(totalCost));
        userRepository.updateBalance(user);


        WalletBalance existingHolding = walletBalanceRepository.getHoldings(userId, coinSymbol);
        if (existingHolding != null) {
            BigDecimal newQty = existingHolding.getQuantity().add(quantityToBuy);
            walletBalanceRepository.updateQuantity(userId, coinSymbol, newQty);
        } else {
            walletBalanceRepository.insertHolding(userId, coinSymbol, quantityToBuy);
        }


        Transaction buyTx = new Transaction();
        buyTx.setUserId(userId);
        buyTx.setCoinSymbol(coinSymbol);
        buyTx.setType(TransactionType.BUY);
        buyTx.setQuantity(quantityToBuy);
        buyTx.setPriceAtTime(currentPrice);
        buyTx.setTotalValue(totalCost);
        buyTx.setTimestamp(java.time.LocalDateTime.now());

        transactionRepository.save(buyTx);


    }



    @Override
    public void sellCrypto(Long userId, String coinSymbol, BigDecimal quantityToSell) {

        Cryptocurrency coin = cryptocurrencyRepository.findBySymbol(coinSymbol)
                .orElseThrow(() -> new CoinNotFoundException("Coin not found: " + coinSymbol));

        String krakenPair = coin.getKrakenPair();
        BigDecimal currentPrice = krakenWebSocketService.getLatestPrice(krakenPair);

        if (currentPrice == null) {
            throw new PriceUnavailableException("Current price for " + coinSymbol + " not available.");
        }


        BigDecimal totalValue = currentPrice.multiply(quantityToSell);


        WalletBalance existingHolding = walletBalanceRepository.getHoldings(userId, coinSymbol);
        if (existingHolding == null || existingHolding.getQuantity().compareTo(quantityToSell) < 0) {
            throw new InsufficientHoldingsException("Insufficient " + coinSymbol + " to sell.");
        }


        BigDecimal remainingQty = existingHolding.getQuantity().subtract(quantityToSell);
        if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
            walletBalanceRepository.updateQuantity(userId, coinSymbol, remainingQty);
        } else {
            walletBalanceRepository.deleteHolding(userId, coinSymbol);
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        user.setBalanceUsd(user.getBalanceUsd().add(totalValue));
        userRepository.updateBalance(user);


        Transaction sellTx = new Transaction();
        sellTx.setUserId(userId);
        sellTx.setCoinSymbol(coinSymbol);
        sellTx.setType(TransactionType.SELL);
        sellTx.setQuantity(quantityToSell);
        sellTx.setPriceAtTime(currentPrice);
        sellTx.setTotalValue(totalValue);
        sellTx.setTimestamp(java.time.LocalDateTime.now());

        transactionRepository.save(sellTx);


    }

}

