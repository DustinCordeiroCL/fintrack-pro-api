package com.dustin.fintrack.service;

import com.dustin.fintrack.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User mockUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "fintrack-test-secret-key-256bits-ok!");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);

        mockUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@fintrack.com")
                .password("encoded_password")
                .build();
    }

    @Test
    @DisplayName("Should generate a non-null token for a valid user")
    void shouldGenerateToken() {
        String token = jwtService.generateToken(mockUser);
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Should extract the correct username from a valid token")
    void shouldExtractUsername() {
        String token = jwtService.generateToken(mockUser);
        String extractedEmail = jwtService.extractUsername(token);
        assertThat(extractedEmail).isEqualTo("test@fintrack.com");
    }

    @Test
    @DisplayName("Should return true when token is valid and belongs to the user")
    void shouldValidateToken() {
        String token = jwtService.generateToken(mockUser);
        assertThat(jwtService.isTokenValid(token, mockUser)).isTrue();
    }

    @Test
    @DisplayName("Should return false when token belongs to a different user")
    void shouldRejectTokenFromDifferentUser() {
        User anotherUser = User.builder()
                .email("other@fintrack.com")
                .build();
        String token = jwtService.generateToken(mockUser);
        assertThat(jwtService.isTokenValid(token, anotherUser)).isFalse();
    }

    @Test
    @DisplayName("Should throw exception when token has invalid signature")
    void shouldThrowOnTamperedToken() {
        String token = jwtService.generateToken(mockUser) + "tampered";
        assertThatThrownBy(() -> jwtService.extractUsername(token))
                .isInstanceOf(Exception.class);
    }
}