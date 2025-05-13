package com.crypto.trading_sim.Controllers;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.Services.CryptocurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cryptocurrencies")
public class CryptocurrencyController {

    private final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyController(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }


    @GetMapping
    public ResponseEntity<List<CryptocurrencyDTO>> getAllCryptocurrencies() {
        List<CryptocurrencyDTO> list = cryptocurrencyService.getAllCryptocurrencies();
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{symbol}")
    public ResponseEntity<CryptocurrencyDTO> getBySymbol(@PathVariable String symbol) {
        return cryptocurrencyService.getBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
