package com.crypto.trading_sim.ControllerTests;

import com.crypto.trading_sim.Controllers.CryptocurrencyController;
import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.Services.CryptocurrencyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CryptocurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock private CryptocurrencyService cryptocurrencyService;

    @InjectMocks private CryptocurrencyController cryptocurrencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cryptocurrencyController).build();
    }

    @Test
    void testGetAllCryptocurrencies() throws Exception {
        List<CryptocurrencyDTO> mockList = List.of(
                new CryptocurrencyDTO("BTC", "Bitcoin", 1, "logo1.png", "BTC/USD", new BigDecimal("50000.00"), List.of()),
                new CryptocurrencyDTO("ETH", "Ethereum", 2, "logo2.png", "ETH/USD", new BigDecimal("1800.00"), List.of())
        );

        when(cryptocurrencyService.getAllCryptocurrencies()).thenReturn(mockList);

        mockMvc.perform(get("/api/cryptocurrencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].symbol").value("BTC"))
                .andExpect(jsonPath("$[1].symbol").value("ETH"));
    }

    @Test
    void testGetBySymbol_Found() throws Exception {
        CryptocurrencyDTO btcDto = new CryptocurrencyDTO(
                "BTC", "Bitcoin", 1, "logo.png", "BTC/USD", new BigDecimal("50000.00"), List.of()
        );

        when(cryptocurrencyService.getBySymbol("BTC")).thenReturn(Optional.of(btcDto));

        mockMvc.perform(get("/api/cryptocurrencies/BTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("BTC"))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetBySymbol_NotFound() throws Exception {
        when(cryptocurrencyService.getBySymbol("DOGE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cryptocurrencies/DOGE"))
                .andExpect(status().isNotFound());
    }
}

