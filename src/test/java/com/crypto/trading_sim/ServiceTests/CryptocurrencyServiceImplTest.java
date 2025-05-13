package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.DTOs.PriceSnapshotDTO;
import com.crypto.trading_sim.Mappers.PriceSnapshotMapper;
import com.crypto.trading_sim.Models.Cryptocurrency;
import com.crypto.trading_sim.Repositories.CryptocurrencyRepository;
import com.crypto.trading_sim.Repositories.PriceSnapshotRepository;
import com.crypto.trading_sim.Services.CryptocurrencyServiceImpl;
import com.crypto.trading_sim.Services.KrakenWebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CryptocurrencyServiceImplTest {

    @Mock
    private CryptocurrencyRepository cryptocurrencyRepository;
    @Mock
    private PriceSnapshotRepository priceSnapshotRepository;
    @Mock
    private PriceSnapshotMapper priceSnapshotMapper;
    @Mock
    private KrakenWebSocketService krakenWebSocketService;
    @Mock
    private com.crypto.trading_sim.Mappers.CryptocurrencyMapper cryptocurrencyMapper;

    @InjectMocks
    private CryptocurrencyServiceImpl cryptocurrencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCryptocurrencies() {
        List<Cryptocurrency> mockCoins = new ArrayList<>(List.of(
                new Cryptocurrency("BTC", "Bitcoin", 1, "logo", "BTC/USD")
        ));

        Map<String, BigDecimal> mockPrices = Map.of("BTC/USD", new BigDecimal("50000.00"));
        List<PriceSnapshotDTO> mockSnapshots = List.of(
                new PriceSnapshotDTO(
                        1L,
                        "BTC",
                        new BigDecimal("50000.00"),
                        LocalDateTime.now().minusDays(1)
                )
        );

        when(cryptocurrencyRepository.findAll()).thenReturn(mockCoins);
        when(krakenWebSocketService.getAllLatestPrices()).thenReturn(mockPrices);
        when(priceSnapshotRepository.findSnapshotsForCoin("BTC", 7)).thenReturn(List.of());
        when(priceSnapshotMapper.toDto(any())).thenReturn(mockSnapshots.get(0));

        List<CryptocurrencyDTO> result = cryptocurrencyService.getAllCryptocurrencies();

        assertEquals(1, result.size());
        assertEquals("BTC", result.get(0).symbol());
        assertEquals(new BigDecimal("50000.00"), result.get(0).currentPrice());
    }


    @Test
    void testGetBySymbol_Found() {
        Cryptocurrency coin = new Cryptocurrency("ETH", "Ethereum", 2, "logo", "ETH/USD");
        BigDecimal price = new BigDecimal("1800.00");
        List<PriceSnapshotDTO> snapshots = List.of(
                new PriceSnapshotDTO(
                        1L,
                        "BTC",
                        new BigDecimal("50000.00"),
                        LocalDateTime.now().minusDays(1)
                )
        );

        when(cryptocurrencyRepository.findBySymbol("ETH")).thenReturn(Optional.of(coin));
        when(krakenWebSocketService.getAllLatestPrices()).thenReturn(Map.of("ETH/USD", price));
        when(priceSnapshotRepository.findSnapshotsForCoin("ETH", 7)).thenReturn(List.of());
        when(priceSnapshotMapper.toDto(any())).thenReturn(snapshots.get(0));

        Optional<CryptocurrencyDTO> result = cryptocurrencyService.getBySymbol("ETH");

        assertTrue(result.isPresent());
        assertEquals("ETH", result.get().symbol());
        assertEquals(price, result.get().currentPrice());
    }

    @Test
    void testGetBySymbol_NotFound() {
        when(cryptocurrencyRepository.findBySymbol("XYZ")).thenReturn(Optional.empty());
        Optional<CryptocurrencyDTO> result = cryptocurrencyService.getBySymbol("XYZ");
        assertFalse(result.isPresent());
    }

    @Test
    void testPreloadStaticData() {
        Cryptocurrency mockCoin = new Cryptocurrency("BTC", "Bitcoin", 1, "logo", "BTC/USD");
        doNothing().when(cryptocurrencyRepository).save(any(Cryptocurrency.class));




        cryptocurrencyService.preloadStaticData();

        verify(cryptocurrencyRepository, atLeastOnce()).save(any(Cryptocurrency.class));
    }

    @Test
    void testInit_WhenEmpty() {
        when(cryptocurrencyRepository.findAll()).thenReturn(Collections.emptyList());

        cryptocurrencyService.init();

        verify(cryptocurrencyRepository, atLeastOnce()).save(any(Cryptocurrency.class));
    }

    @Test
    void testInit_WhenNotEmpty() {
        when(cryptocurrencyRepository.findAll()).thenReturn(List.of(new Cryptocurrency("BTC", "Bitcoin", 1, "logo", "BTC/USD")));

        cryptocurrencyService.init();

        verify(cryptocurrencyRepository, never()).save(any());
    }
}

