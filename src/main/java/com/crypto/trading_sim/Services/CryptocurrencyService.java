package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;

import java.util.List;
import java.util.Optional;

public interface CryptocurrencyService {

    List<CryptocurrencyDTO> getAllCryptocurrencies();

    Optional<CryptocurrencyDTO> getBySymbol(String symbol);

    void preloadStaticData();
}
