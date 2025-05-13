package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.CryptoHoldingDTO;
import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.DTOs.WalletBalanceDTO;
import com.crypto.trading_sim.Models.WalletBalance;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WalletBalanceServiceImpl implements WalletBalanceService {

    private final WalletBalanceRepository walletBalanceRepository;
    private final CryptocurrencyService cryptocurrencyService;

    public WalletBalanceServiceImpl(WalletBalanceRepository walletBalanceRepository,CryptocurrencyService cryptocurrencyService) {
        this.walletBalanceRepository = walletBalanceRepository;
        this.cryptocurrencyService=cryptocurrencyService;
    }



    @Override
    public List<CryptoHoldingDTO> getHoldingsForUser(Long userId) {
        List<WalletBalance> holdings = walletBalanceRepository.getCryptoHoldingsForUser(userId);

        return holdings.stream()
                .map(holding -> {
                    Optional<CryptocurrencyDTO> coinOpt = cryptocurrencyService.getBySymbol(holding.getCoinSymbol());

                    if (coinOpt.isPresent()) {
                        CryptocurrencyDTO coin = coinOpt.get();
                        return new CryptoHoldingDTO(
                                coin.symbol(),
                                coin.name(),
                                coin.logoUrl(),
                                coin.currentPrice(),
                                holding.getQuantity()
                        );
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
