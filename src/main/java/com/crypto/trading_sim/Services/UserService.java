package com.crypto.trading_sim.Services;

import com.crypto.trading_sim.DTOs.UserDTO;


import java.math.BigDecimal;
import java.util.Optional;


public interface UserService {

    UserDTO createUser(UserDTO dto);

    Optional<UserDTO> getUserById(Long userId);

    void deleteUser(Long id);

    void resetUserAccount(Long id);

    Long getUserIdFromEmail(String email);

    BigDecimal getUserBalance(Long userId);

}
