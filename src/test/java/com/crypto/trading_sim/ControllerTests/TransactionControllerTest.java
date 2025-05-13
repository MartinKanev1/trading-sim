package com.crypto.trading_sim.ControllerTests;

import com.crypto.trading_sim.Controllers.TransactionController;
import com.crypto.trading_sim.DTOs.TransactionDTO;
import com.crypto.trading_sim.Models.TransactionType;
import com.crypto.trading_sim.Services.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock private TransactionService transactionService;

    @InjectMocks private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testGetUserTransactions_ReturnsList() throws Exception {
        Long userId = 1L;

        List<TransactionDTO> mockTransactions = List.of(
                new TransactionDTO(1L, userId, "BTC", TransactionType.BUY,
                        new BigDecimal("0.01"), new BigDecimal("50000.00"),
                        new BigDecimal("500.00"), LocalDateTime.now(), new BigDecimal("0.00")),

                new TransactionDTO(2L, userId, "ETH", TransactionType.SELL,
                        new BigDecimal("0.5"), new BigDecimal("1800.00"),
                        new BigDecimal("900.00"), LocalDateTime.now(), new BigDecimal("50.00"))
        );

        when(transactionService.getTransactionsForUser(userId)).thenReturn(mockTransactions);

        mockMvc.perform(get("/api/transactions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].coinSymbol").value("BTC"))
                .andExpect(jsonPath("$[0].profitLoss").value(0.00))
                .andExpect(jsonPath("$[1].coinSymbol").value("ETH"))
                .andExpect(jsonPath("$[1].profitLoss").value(50.00));
    }

    @Test
    void testGetUserTransactions_EmptyList() throws Exception {
        when(transactionService.getTransactionsForUser(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/user/2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}

