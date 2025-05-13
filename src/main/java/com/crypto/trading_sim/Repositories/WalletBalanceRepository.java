package com.crypto.trading_sim.Repositories;

import com.crypto.trading_sim.Models.WalletBalance;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class WalletBalanceRepository {
    private final JdbcTemplate jdbcTemplate;

    public WalletBalanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM wallet_balances WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public WalletBalance getHoldings(Long userId, String coinSymbol) {
        String sql = "SELECT * FROM wallet_balances WHERE user_id = ? AND coin_symbol = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, coinSymbol}, (rs, rowNum) ->
                    new WalletBalance(
                            rs.getLong("user_id"),
                            rs.getString("coin_symbol"),
                            rs.getBigDecimal("quantity")
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateQuantity(Long userId, String coinSymbol, BigDecimal newQuantity) {
        String sql = "UPDATE wallet_balances SET quantity = ? WHERE user_id = ? AND coin_symbol = ?";
        jdbcTemplate.update(sql, newQuantity, userId, coinSymbol);
    }

    public void insertHolding(Long userId, String coinSymbol, BigDecimal quantity) {
        String sql = "INSERT INTO wallet_balances (user_id, coin_symbol, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, coinSymbol, quantity);
    }

    public List<WalletBalance> getCryptoHoldingsForUser(Long userId) {
        String sql = "SELECT * FROM wallet_balances WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new WalletBalance(
                        rs.getLong("user_id"),
                        rs.getString("coin_symbol"),
                        rs.getBigDecimal("quantity")
                )
        );
    }

    public void deleteHolding(Long userId, String coinSymbol) {
        String sql = "DELETE FROM wallet_balances WHERE user_id = ? AND coin_symbol = ?";
        jdbcTemplate.update(sql, userId, coinSymbol);
    }





}
