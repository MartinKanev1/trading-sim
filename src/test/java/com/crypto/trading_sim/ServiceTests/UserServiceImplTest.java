package com.crypto.trading_sim.ServiceTests;

import com.crypto.trading_sim.DTOs.UserDTO;
import com.crypto.trading_sim.Exceptions.UserNotFoundException;
import com.crypto.trading_sim.Mappers.UserMapper;
import com.crypto.trading_sim.Models.User;
import com.crypto.trading_sim.Repositories.TransactionRepository;
import com.crypto.trading_sim.Repositories.UserRepository;
import com.crypto.trading_sim.Repositories.WalletBalanceRepository;
import com.crypto.trading_sim.Services.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private WalletBalanceRepository walletBalanceRepository;
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserDTO dto = new UserDTO(null, "test@example.com", "password", new BigDecimal("0.00"));
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setBalanceUsd(new BigDecimal("10000.00"));

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDTO(1L, user.getEmail(), user.getPassword(), user.getBalanceUsd()));

        UserDTO result = userService.createUser(dto);

        assertEquals("test@example.com", result.email());
        assertEquals(new BigDecimal("10000.00"), result.balanceUsd());
    }

    @Test
    void testCreateUser_EmailExists() {
        UserDTO dto = new UserDTO(null, "exists@example.com", "password", BigDecimal.ZERO);

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    @Test
    void testGetUserById_Found() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDTO(1L, "user@example.com", "pass", BigDecimal.ZERO));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().email());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testGetUserIdFromEmail() {
        when(userRepository.findIdByEmail("john@example.com")).thenReturn(42L);
        Long userId = userService.getUserIdFromEmail("john@example.com");
        assertEquals(42L, userId);
    }

    @Test
    void testGetUserBalance() {
        when(userRepository.getBalanceById(1L)).thenReturn(new BigDecimal("9999.99"));
        BigDecimal balance = userService.getUserBalance(1L);
        assertEquals(new BigDecimal("9999.99"), balance);
    }

    @Test
    void testResetUserAccount_Success() {
        User user = new User();
        user.setId(1L);
        user.setBalanceUsd(new BigDecimal("400.00"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.resetUserAccount(1L);

        verify(walletBalanceRepository).deleteByUserId(1L);
        verify(transactionRepository).deleteByUserId(1L);
        verify(userRepository).updateBalance(argThat(u -> u.getBalanceUsd().equals(new BigDecimal("10000.00"))));
    }

    @Test
    void testResetUserAccount_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.resetUserAccount(1L));
    }
}
