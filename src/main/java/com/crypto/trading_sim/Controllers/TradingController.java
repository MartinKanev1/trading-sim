package com.crypto.trading_sim.Controllers;

import com.crypto.trading_sim.Services.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
public class TradingController {

    private final TradingService tradingService;

    @Autowired
    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyCrypto(@RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        String coinSymbol = request.get("coinSymbol");
        BigDecimal quantity = new BigDecimal(request.get("quantity"));

        tradingService.buyCrypto(userId, coinSymbol, quantity);
        return ResponseEntity.ok("Successfully bought " + quantity + " " + coinSymbol);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCrypto(@RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        String coinSymbol = request.get("coinSymbol");
        BigDecimal quantity = new BigDecimal(request.get("quantity"));

        tradingService.sellCrypto(userId, coinSymbol, quantity);
        return ResponseEntity.ok("Successfully sold " + quantity + " " + coinSymbol);
    }
}
