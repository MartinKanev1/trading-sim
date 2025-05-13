package com.crypto.trading_sim.Services;

import java.math.BigDecimal;

public interface TradingService {

    void buyCrypto(Long userId, String coinSymbol, BigDecimal quantityToBuy);


    void sellCrypto(Long userId, String coinSymbol, BigDecimal quantityToSell);
}
