package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.DTOs.PriceSnapshotDTO;
import com.crypto.trading_sim.Mappers.CryptocurrencyMapper;
import com.crypto.trading_sim.Mappers.PriceSnapshotMapper;
import com.crypto.trading_sim.Models.Cryptocurrency;
import com.crypto.trading_sim.Repositories.CryptocurrencyRepository;
import com.crypto.trading_sim.Repositories.PriceSnapshotRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CryptocurrencyServiceImpl implements CryptocurrencyService {

    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final CryptocurrencyMapper cryptocurrencyMapper;
    private final KrakenWebSocketService krakenWebSocketService;
    private final PriceSnapshotMapper priceSnapshotMapper;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public CryptocurrencyServiceImpl(CryptocurrencyRepository cryptocurrencyRepository,
                                     CryptocurrencyMapper cryptocurrencyMapper,
                                     KrakenWebSocketService krakenWebSocketService,
                                     PriceSnapshotMapper priceSnapshotMapper,
                                     PriceSnapshotRepository priceSnapshotRepository) {


        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.cryptocurrencyMapper = cryptocurrencyMapper;
        this.krakenWebSocketService=krakenWebSocketService;
        this.priceSnapshotMapper=priceSnapshotMapper;
        this.priceSnapshotRepository=priceSnapshotRepository;
    }



    @Override
    public List<CryptocurrencyDTO> getAllCryptocurrencies() {

        List<Cryptocurrency> coins = cryptocurrencyRepository.findAll();

        coins.sort(Comparator.comparingInt(Cryptocurrency::getRank));


        Map<String, BigDecimal> currentPrices = krakenWebSocketService.getAllLatestPrices();


        return coins.stream().map(coin -> {
            String symbol = coin.getSymbol();
            String krakenPair = coin.getKrakenPair();


            BigDecimal currentPrice = currentPrices.getOrDefault(krakenPair, BigDecimal.ZERO);


            List<PriceSnapshotDTO> snapshots = priceSnapshotRepository
                    .findSnapshotsForCoin(symbol, 7)
                    .stream()
                    .map(priceSnapshotMapper::toDto)
                    .toList();

            return new CryptocurrencyDTO(
                    coin.getSymbol(),
                    coin.getName(),
                    coin.getRank(),
                    coin.getLogoUrl(),
                    coin.getKrakenPair(),
                    currentPrice,
                    snapshots
            );
        }).toList();
    }




    @Override
    public Optional<CryptocurrencyDTO> getBySymbol(String symbol) {
        return cryptocurrencyRepository.findBySymbol(symbol).map(coin -> {
            String krakenPair = coin.getKrakenPair();


            BigDecimal currentPrice = krakenWebSocketService
                    .getAllLatestPrices()
                    .getOrDefault(krakenPair, BigDecimal.ZERO);


            List<PriceSnapshotDTO> snapshots = priceSnapshotRepository
                    .findSnapshotsForCoin(symbol, 7)
                    .stream()
                    .map(priceSnapshotMapper::toDto)
                    .toList();


            return new CryptocurrencyDTO(
                    coin.getSymbol(),
                    coin.getName(),
                    coin.getRank(),
                    coin.getLogoUrl(),
                    coin.getKrakenPair(),
                    currentPrice,
                    snapshots
            );
        });
    }


    @Override
    public void preloadStaticData() {
        List<Cryptocurrency> coins = List.of(
                new Cryptocurrency("BTC", "Bitcoin", 1, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/bitcoin-btc-logo_ahrrs1.png", "BTC/USD"),
                new Cryptocurrency("ETH", "Ethereum", 2, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/ethereum-eth-logo_vdnkus.png", "ETH/USD"),
                new Cryptocurrency("USDT", "Tether", 3, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/tether-usdt-logo_xk1omy.png", "USDT/USD"),
                new Cryptocurrency("BNB", "BNB", 4, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/bnb-bnb-logo_k8konc.png", "BNB/USD"),
                new Cryptocurrency("SOL", "Solana", 5, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/solana-sol-logo_vycnvm.png", "SOL/USD"),
                new Cryptocurrency("XRP", "XRP", 6, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985531/xrp-xrp-logo_axwmi2.png", "XRP/USD"),
                new Cryptocurrency("USDC", "USD Coin", 7, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/usd-coin-usdc-logo_lhcrpp.png", "USDC/USD"),
                new Cryptocurrency("DOGE", "Dogecoin", 8, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/dogecoin-doge-logo_o1ndq4.png", "DOGE/USD"),
                new Cryptocurrency("TON", "Toncoin", 9, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/toncoin-ton-logo_v16lzt.png", "TON/USD"),
                new Cryptocurrency("ADA", "Cardano", 10, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/cardano-ada-logo_edixiq.png", "ADA/USD"),
                new Cryptocurrency("AVAX", "Avalanche", 11, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/avalanche-avax-logo_bmeuff.png", "AVAX/USD"),
                new Cryptocurrency("SHIB", "Shiba Inu", 12, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/shiba-inu-shib-logo_lnk8gf.png", "SHIB/USD"),
                new Cryptocurrency("DOT", "Polkadot", 13, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/polkadot-new-dot-logo_yme2lb.png", "DOT/USD"),
                new Cryptocurrency("TRX", "TRON", 14, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/tron-trx-logo_gce1d3.png", "TRX/USD"),
                new Cryptocurrency("WBTC", "Wrapped Bitcoin", 15, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985530/wrapped-bitcoin-wbtc-logo_yydjuu.png", "WBTC/USD"),
                new Cryptocurrency("LINK", "Chainlink", 16, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/chainlink-link-logo_fkjmep.png", "LINK/USD"),
                new Cryptocurrency("MATIC", "Polygon", 17, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/polygon-matic-logo_k0mq6g.png", "MATIC/USD"),
                new Cryptocurrency("BCH", "Bitcoin Cash", 18, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/bitcoin-cash-bch-logo_dwy1db.png", "BCH/USD"),
                new Cryptocurrency("LTC", "Litecoin", 19, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/litecoin-ltc-logo_umfd6i.png", "LTC/USD"),
                new Cryptocurrency("ICP", "Internet Computer", 20, "https://res.cloudinary.com/dxm9f2wne/image/upload/v1746985529/internet-computer-icp-logo_nndwla.png", "ICP/USD")
        );

        for (Cryptocurrency coin : coins) {
            cryptocurrencyRepository.save(coin);
        }
    }

    @PostConstruct
    public void init() {
        if (cryptocurrencyRepository.findAll().isEmpty()) {
            preloadStaticData();
        }
    }


}
