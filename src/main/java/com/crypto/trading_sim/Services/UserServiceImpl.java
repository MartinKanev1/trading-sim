package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.UserDTO;
import com.crypto.trading_sim.Exceptions.UserNotFoundException;
import com.crypto.trading_sim.Mappers.UserMapper;
import com.crypto.trading_sim.Models.User;
import com.crypto.trading_sim.Repositories.TransactionRepository;
import com.crypto.trading_sim.Repositories.UserRepository;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final WalletBalanceRepository walletBalanceRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    public UserServiceImpl(UserRepository userRepository,UserMapper userMapper,WalletBalanceRepository walletBalanceRepository,TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.walletBalanceRepository= walletBalanceRepository;
        this.transactionRepository=transactionRepository;
    }

    public UserDTO createUser(UserDTO dto) {
        User user = new User();

        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setBalanceUsd(new BigDecimal("10000.00"));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public Optional<UserDTO> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(userMapper::toDto);
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));


        userRepository.delete(existingUser);
    }

    public Long getUserIdFromEmail(String email) {
        return userRepository.findIdByEmail(email);


    }

    public BigDecimal getUserBalance(Long userId) {
        return userRepository.getBalanceById(userId);
    }

    @Transactional
    public void resetUserAccount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        walletBalanceRepository.deleteByUserId(userId);


        transactionRepository.deleteByUserId(userId);


        user.setBalanceUsd(new BigDecimal("10000.00"));
        userRepository.updateBalance(user);

    }

}
