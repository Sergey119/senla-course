package com.example.task.controller;

import com.example.task.dto.AuthRequest;
import com.example.task.security.jwt.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void login_WithExistingOrdinaryUserCredentials_ShouldRespondWith200() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("true_user");
        authRequest.setPassword("true_user_password123");
        String mockToken = "$2a$10$qmI8EqBD7rbVYmr0aS3j/.YDcFirsEtJC/i7ApVl/WpI.b2TJUhFC";

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()
                        ));

        UserDetails userDetails = User
                .withUsername(authRequest.getUsername())
                .password(authRequest.getPassword())
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(mockToken);

        mockMvc.perform(
                        post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(authRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(
                        new MediaType(MediaType.APPLICATION_JSON))
                );
    }

    @Test
    public void login_WithNonExistingCredentials_ShouldRespondWith401() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("non-existing-username");
        authRequest.setPassword("non-existing-password");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        mockMvc.perform(
                post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(
                        new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.ISO_8859_1))
                );
    }
}
