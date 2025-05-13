package com.crypto.trading_sim.Repositories;

import com.crypto.trading_sim.Models.PriceSnapshot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceSnapshotRepository {

    private final JdbcTemplate jdbcTemplate;

    public PriceSnapshotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert a new snapshot
    public void save(PriceSnapshot snapshot) {
        String sql = "INSERT INTO price_snapshots (coin_symbol, price, timestamp) VALUES (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                snapshot.getCoinSymbol(),
                snapshot.getPrice(),
                Timestamp.valueOf(snapshot.getTimestamp())
        );
    }



    public List<PriceSnapshot> findSnapshotsForCoin(String coinSymbol, int days) {

        String sql = """
        SELECT id, coin_symbol, price, timestamp
        FROM price_snapshots
        WHERE coin_symbol = ?
          AND timestamp >= NOW() - INTERVAL '%s DAY'
        ORDER BY timestamp ASC
        """.formatted(days);



        return jdbcTemplate.query(
                sql,
                new Object[]{coinSymbol},
                priceSnapshotRowMapper()
        );
    }



    private RowMapper<PriceSnapshot> priceSnapshotRowMapper() {
        return (rs, rowNum) -> new PriceSnapshot(
                rs.getLong("id"),
                rs.getString("coin_symbol"),
                rs.getBigDecimal("price"),
                rs.getTimestamp("timestamp").toLocalDateTime()
        );
    }
}
