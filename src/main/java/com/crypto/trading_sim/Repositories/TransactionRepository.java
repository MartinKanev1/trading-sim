package com.crypto.trading_sim.Repositories;

import com.crypto.trading_sim.Models.Transaction;
import com.crypto.trading_sim.Models.TransactionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM transactions WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void save(Transaction tx) {
        String sql = "INSERT INTO transactions (user_id, coin_symbol, type, quantity, price_at_time, total_value, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                tx.getUserId(),
                tx.getCoinSymbol(),
                tx.getType().toString(),
                tx.getQuantity(),
                tx.getPriceAtTime(),
                tx.getTotalValue(),
                Timestamp.valueOf(tx.getTimestamp())
        );
    }

    public List<Transaction> getAllByUserId(Long userId) {
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new Transaction(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("coin_symbol"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getBigDecimal("quantity"),
                rs.getBigDecimal("price_at_time"),
                rs.getBigDecimal("total_value"),
                rs.getTimestamp("timestamp").toLocalDateTime()
        ));
    }


}
