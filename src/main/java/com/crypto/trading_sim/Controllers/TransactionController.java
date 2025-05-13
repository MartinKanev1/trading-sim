package com.crypto.trading_sim.Controllers;

import com.crypto.trading_sim.DTOs.TransactionDTO;
import com.crypto.trading_sim.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/user/{userId}")
    public List<TransactionDTO> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getTransactionsForUser(userId);
    }
}
