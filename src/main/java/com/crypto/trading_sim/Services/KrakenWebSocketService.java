package com.crypto.trading_sim.Services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KrakenWebSocketService implements WebSocket.Listener {

    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com/v2";
    private static final String[] PAIRS = {
            "BTC/USD", "ETH/USD", "USDT/USD", "BNB/USD", "SOL/USD",
            "XRP/USD", "USDC/USD", "DOGE/USD", "TON/USD", "ADA/USD",
            "AVAX/USD", "SHIB/USD", "DOT/USD", "TRX/USD", "WBTC/USD",
            "LINK/USD", "MATIC/USD", "BCH/USD", "LTC/USD", "ICP/USD"
    };

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, BigDecimal> latestPrices = new ConcurrentHashMap<>();
    private WebSocket webSocket;

    @PostConstruct
    public void connect() {



        HttpClient client = HttpClient.newHttpClient();
        client.newWebSocketBuilder()
                .buildAsync(URI.create(KRAKEN_WS_URL), this)
                .thenAccept(ws -> {
                    this.webSocket = ws;
                    subscribeToTicker();
                });
    }

    private void subscribeToTicker() {
        String json = """
        {
          "method": "subscribe",
          "params": {
            "channel": "ticker",
            "snapshot": true,
            "symbol": [ %s ]
          }
        }
        """.formatted(String.join(",", wrapInQuotes(PAIRS)));

        webSocket.sendText(json, true);



    }

    private String[] wrapInQuotes(String[] input) {
        String[] quoted = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            quoted[i] = "\"" + input[i] + "\"";
        }
        return quoted;
    }



    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            JsonNode node = objectMapper.readTree(data.toString());

            if (node.has("channel") && "ticker".equals(node.get("channel").asText())) {
                JsonNode dataArray = node.get("data");
                if (dataArray.isArray()) {
                    for (JsonNode item : dataArray) {
                        String symbol = item.get("symbol").asText();
                        BigDecimal price = new BigDecimal(item.get("last").asText());


                        latestPrices.put(symbol, price);

                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error parsing Kraken message: " + e.getMessage());
        }

        webSocket.request(1);
        return CompletableFuture.completedFuture(null);
    }




    public BigDecimal getLatestPrice(String krakenPair) {

        return latestPrices.get(krakenPair);
    }


    public String getKrakenPairBySymbol(String symbol) {
        return symbol + "/USD";
    }




    public Map<String, BigDecimal> getAllLatestPrices() {

        Map<String, BigDecimal> krakenPrices = this.latestPrices;

        return krakenPrices;
    }


}
