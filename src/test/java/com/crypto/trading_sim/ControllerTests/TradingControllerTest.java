package com.crypto.trading_sim.ControllerTests;

import com.crypto.trading_sim.Controllers.TradingController;
import com.crypto.trading_sim.Services.TradingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TradingControllerTest {

    private MockMvc mockMvc;

    @Mock private TradingService tradingService;

    @InjectMocks private TradingController tradingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tradingController).build();
    }

    @Test
    void testBuyCrypto() throws Exception {
        String jsonRequest = """
            {
                "userId": "1",
                "coinSymbol": "BTC",
                "quantity": "0.01"
            }
        """;

        doNothing().when(tradingService).buyCrypto(1L, "BTC", new java.math.BigDecimal("0.01"));

        mockMvc.perform(post("/api/trade/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully bought 0.01 BTC"));

        verify(tradingService).buyCrypto(1L, "BTC", new java.math.BigDecimal("0.01"));
    }

    @Test
    void testSellCrypto() throws Exception {
        String jsonRequest = """
            {
                "userId": "2",
                "coinSymbol": "ETH",
                "quantity": "0.5"
            }
        """;

        doNothing().when(tradingService).sellCrypto(2L, "ETH", new java.math.BigDecimal("0.5"));

        mockMvc.perform(post("/api/trade/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully sold 0.5 ETH"));

        verify(tradingService).sellCrypto(2L, "ETH", new java.math.BigDecimal("0.5"));
    }
}
