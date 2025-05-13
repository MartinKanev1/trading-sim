package com.crypto.trading_sim.ControllerTests;

import com.crypto.trading_sim.Controllers.WalletBalanceController;
import com.crypto.trading_sim.DTOs.CryptoHoldingDTO;
import com.crypto.trading_sim.Services.WalletBalanceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WalletBalanceControllerTest {

    private MockMvc mockMvc;

    @Mock private WalletBalanceService walletBalanceService;

    @InjectMocks private WalletBalanceController walletBalanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletBalanceController).build();
    }

    @Test
    void testGetUserHoldings_ReturnsHoldings() throws Exception {
        Long userId = 1L;

        List<CryptoHoldingDTO> mockHoldings = List.of(
                new CryptoHoldingDTO("BTC", "Bitcoin", "logo.png", new BigDecimal("50000.00"), new BigDecimal("0.01")),
                new CryptoHoldingDTO("ETH", "Ethereum", "eth_logo.png", new BigDecimal("1800.00"), new BigDecimal("0.5"))
        );

        when(walletBalanceService.getHoldingsForUser(userId)).thenReturn(mockHoldings);

        mockMvc.perform(get("/api/wallet/1/holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("BTC"))
                .andExpect(jsonPath("$[0].quantity").value(0.01))
                .andExpect(jsonPath("$[1].symbol").value("ETH"))
                .andExpect(jsonPath("$[1].quantity").value(0.5));
    }

    @Test
    void testGetUserHoldings_EmptyList() throws Exception {
        when(walletBalanceService.getHoldingsForUser(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/wallet/2/holdings"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
