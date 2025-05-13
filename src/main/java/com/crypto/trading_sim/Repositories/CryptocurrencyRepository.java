package com.crypto.trading_sim.Repositories;

import com.crypto.trading_sim.DTOs.CryptocurrencyDTO;
import com.crypto.trading_sim.Models.Cryptocurrency;
import com.crypto.trading_sim.Models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CryptocurrencyRepository {
    private final JdbcTemplate jdbcTemplate;

    public CryptocurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cryptocurrency> findAll() {
        String sql = "SELECT * FROM cryptocurrencies";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Cryptocurrency(
                rs.getString("symbol"),
                rs.getString("name"),
                rs.getInt("rank"),
                rs.getString("logo_url"),
                rs.getString("kraken_pair")
        ));
    }

    public Optional<Cryptocurrency> findBySymbol(String symbol) {
        String sql = "SELECT * FROM cryptocurrencies WHERE symbol = ?";

        try {
            Cryptocurrency coin = jdbcTemplate.queryForObject(sql, new Object[]{symbol}, (rs, rowNum) -> new Cryptocurrency(
                    rs.getString("symbol"),
                    rs.getString("name"),
                    rs.getInt("rank"),
                    rs.getString("logo_url"),
                    rs.getString("kraken_pair")
            ));
            return Optional.of(coin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Cryptocurrency coin) {
        String sql = "INSERT INTO cryptocurrencies (symbol, name, rank, logo_url, kraken_pair) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                coin.getSymbol(),
                coin.getName(),
                coin.getRank(),
                coin.getLogoUrl(),
                coin.getKrakenPair()
        );
    }




}
