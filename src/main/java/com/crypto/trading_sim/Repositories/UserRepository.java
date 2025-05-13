package com.crypto.trading_sim.Repositories;

import com.crypto.trading_sim.Exceptions.UserNotFoundException;
import com.crypto.trading_sim.Models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public User save(User user) {
        String sql = "INSERT INTO users (email, password, balance_usd) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setBigDecimal(3, user.getBalanceUsd());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            user.setId(((Number) keys.get("id")).longValue());
        }

        return user;
    }


    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    new User(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBigDecimal("balance_usd")
                    )
            );
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long findIdByEmail(String email) {
        try {
            String sql = "SELECT id FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, Long.class);
        } catch (UserNotFoundException e) {
            return null;
        }
    }


    public void delete(User user) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    public void updateBalance(User user) {
        String sql = "UPDATE users SET balance_usd = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getBalanceUsd(), user.getId());
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }


    public BigDecimal getBalanceById(Long userId) {

        try {
            String sql = "SELECT balance_usd FROM users WHERE id =?";
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, BigDecimal.class);
        } catch (UserNotFoundException e) {
            return null;
        }
    }
}
