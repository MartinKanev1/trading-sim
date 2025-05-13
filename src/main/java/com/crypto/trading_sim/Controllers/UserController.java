package com.crypto.trading_sim.Controllers;

import com.crypto.trading_sim.DTOs.UserDTO;
import com.crypto.trading_sim.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        UserDTO created = userService.createUser(userDto);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{userId}/reset")
    public ResponseEntity<Void> resetUserAccount(@PathVariable Long userId) {
        userService.resetUserAccount(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id-by-email/{email}")
    public ResponseEntity<?> getIdByEmail(@PathVariable String email) {
        Long userId = userService.getUserIdFromEmail(email);
        if (userId != null) {
            return ResponseEntity.ok().body(Map.of("userId", userId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        BigDecimal balance = userService.getUserBalance(userId);
        return ResponseEntity.ok(balance);
    }

}
