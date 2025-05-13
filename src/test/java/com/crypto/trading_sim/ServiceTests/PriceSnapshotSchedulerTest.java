package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.Models.PriceSnapshot;
import com.crypto.trading_sim.Repositories.PriceSnapshotRepository;
import com.crypto.trading_sim.Services.KrakenWebSocketService;
import com.crypto.trading_sim.Services.PriceSnapshotScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PriceSnapshotSchedulerTest {

    @Mock
    private KrakenWebSocketService krakenService;

    @Mock
    private PriceSnapshotRepository snapshotRepository;

    @InjectMocks
    private PriceSnapshotScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCapturePriceSnapshots() {
        Map<String, BigDecimal> mockPrices = Map.of(
                "BTC/USD", new BigDecimal("50000.00"),
                "ETH/USD", new BigDecimal("1800.00")
        );

        when(krakenService.getAllLatestPrices()).thenReturn(mockPrices);

        scheduler.capturePriceSnapshots();


        verify(snapshotRepository, times(2)).save(any(PriceSnapshot.class));


        ArgumentCaptor<PriceSnapshot> captor = ArgumentCaptor.forClass(PriceSnapshot.class);
        verify(snapshotRepository, times(2)).save(captor.capture());

        for (PriceSnapshot snapshot : captor.getAllValues()) {
            assertNotNull(snapshot.getTimestamp());
            assertTrue(snapshot.getCoinSymbol().equals("BTC") || snapshot.getCoinSymbol().equals("ETH"));
            assertTrue(snapshot.getPrice().equals(new BigDecimal("50000.00")) || snapshot.getPrice().equals(new BigDecimal("1800.00")));
        }
    }
}
