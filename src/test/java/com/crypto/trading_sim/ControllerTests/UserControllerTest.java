package com.crypto.trading_sim.ControllerTests;



import com.crypto.trading_sim.Controllers.UserController;
import com.crypto.trading_sim.DTOs.UserDTO;
import com.crypto.trading_sim.Services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock private UserService userService;

    @InjectMocks private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO input = new UserDTO(null, "test@example.com", "password", null);
        UserDTO output = new UserDTO(1L, "test@example.com", "password", new BigDecimal("10000.00"));

        when(userService.createUser(any(UserDTO.class))).thenReturn(output);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetUserById_Found() throws Exception {
        UserDTO user = new UserDTO(1L, "user@example.com", "pass", new BigDecimal("5000.00"));
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void testResetUserAccount() throws Exception {
        doNothing().when(userService).resetUserAccount(1L);

        mockMvc.perform(post("/api/users/1/reset"))
                .andExpect(status().isOk());

        verify(userService).resetUserAccount(1L);
    }

    @Test
    void testGetUserIdByEmail_Found() throws Exception {
        when(userService.getUserIdFromEmail("john@example.com")).thenReturn(42L);

        mockMvc.perform(get("/api/users/id-by-email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(42));
    }

    @Test
    void testGetUserIdByEmail_NotFound() throws Exception {
        when(userService.getUserIdFromEmail("missing@example.com")).thenReturn(null);

        mockMvc.perform(get("/api/users/id-by-email/missing@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testGetUserBalance() throws Exception {
        when(userService.getUserBalance(1L)).thenReturn(new BigDecimal("8888.88"));

        mockMvc.perform(get("/api/users/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("8888.88"));
    }
}

