package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.Services.KrakenWebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KrakenWebSocketServiceTest {

    private KrakenWebSocketService service;

    @BeforeEach
    void setUp() {
        service = new KrakenWebSocketService();
    }

    @Test
    void testGetKrakenPairBySymbol() {
        assertEquals("BTC/USD", service.getKrakenPairBySymbol("BTC"));
        assertEquals("ETH/USD", service.getKrakenPairBySymbol("ETH"));
    }

    @Test
    void testGetLatestPriceAndAllPrices() {

        service.getAllLatestPrices().put("BTC/USD", new BigDecimal("50000.00"));

        BigDecimal price = service.getLatestPrice("BTC/USD");
        assertEquals(new BigDecimal("50000.00"), price);

        Map<String, BigDecimal> prices = service.getAllLatestPrices();
        assertTrue(prices.containsKey("BTC/USD"));
    }
}

