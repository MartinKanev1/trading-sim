package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.TransactionDTO;
import com.crypto.trading_sim.Models.Transaction;
import com.crypto.trading_sim.Models.TransactionType;
import com.crypto.trading_sim.Repositories.TransactionRepository;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletBalanceRepository walletBalanceRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            WalletBalanceRepository walletBalanceRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.walletBalanceRepository = walletBalanceRepository;
    }

    @Override
    public List<TransactionDTO> getTransactionsForUser(Long userId) {
        List<Transaction> transactions = transactionRepository.getAllByUserId(userId);

        return transactions.stream().map(tx -> {
            BigDecimal profitLoss = calculateProfitLoss(tx, userId);
            return new TransactionDTO(
                    tx.getId(),
                    tx.getUserId(),
                    tx.getCoinSymbol(),
                    tx.getType(),
                    tx.getQuantity(),
                    tx.getPriceAtTime(),
                    tx.getTotalValue(),
                    tx.getTimestamp(),
                    profitLoss
            );
        }).collect(Collectors.toList());
    }

    private BigDecimal calculateProfitLoss(Transaction tx, Long userId) {

        if (tx.getType() != TransactionType.SELL) {
            return null;
        }


        List<Transaction> buyTxs = transactionRepository.getAllByUserId(userId).stream()
                .filter(t -> t.getCoinSymbol().equals(tx.getCoinSymbol()) && t.getType() == TransactionType.BUY)
                .collect(Collectors.toList());

        if (buyTxs.isEmpty()) {
            return null;
        }

        BigDecimal totalBuyQty = BigDecimal.ZERO;
        BigDecimal totalBuyValue = BigDecimal.ZERO;

        for (Transaction buy : buyTxs) {
            totalBuyQty = totalBuyQty.add(buy.getQuantity());
            totalBuyValue = totalBuyValue.add(buy.getTotalValue());
        }

        if (totalBuyQty.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        BigDecimal avgBuyPrice = totalBuyValue.divide(totalBuyQty, 8, RoundingMode.HALF_UP);

        BigDecimal costBasis = avgBuyPrice.multiply(tx.getQuantity());

        return tx.getTotalValue().subtract(costBasis);
    }
}
