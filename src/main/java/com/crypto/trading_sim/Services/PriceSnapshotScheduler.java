package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.Models.PriceSnapshot;
import com.crypto.trading_sim.Repositories.PriceSnapshotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PriceSnapshotScheduler {

    private final KrakenWebSocketService krakenService;
    private final PriceSnapshotRepository snapshotRepository;

    public PriceSnapshotScheduler(KrakenWebSocketService krakenService,
                                  PriceSnapshotRepository snapshotRepository) {
        this.krakenService = krakenService;
        this.snapshotRepository = snapshotRepository;
    }



    @Scheduled(fixedRate = 60000)
public void capturePriceSnapshots() {
    Map<String, BigDecimal> prices = krakenService.getAllLatestPrices();

    LocalDateTime now = LocalDateTime.now();

    prices.forEach((krakenPair, price) -> {
        PriceSnapshot snapshot = new PriceSnapshot();


        String symbol = krakenPair.split("/")[0];
        snapshot.setCoinSymbol(symbol);

        snapshot.setPrice(price);
        snapshot.setTimestamp(now);

        snapshotRepository.save(snapshot);
    });


}



}
