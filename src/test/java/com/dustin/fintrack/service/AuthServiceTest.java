package com.dustin.fintrack.service;

import com.dustin.fintrack.dto.v1.request.AuthRequestDTO;
import com.dustin.fintrack.dto.v1.request.RegisterRequestDTO;
import com.dustin.fintrack.dto.v1.response.AuthResponseDTO;
import com.dustin.fintrack.model.RefreshToken;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private RefreshTokenService refreshTokenService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private RefreshToken mockRefreshToken;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L).name("Dustin").email("dustin@fintrack.com")
                .password("encoded").build();

        mockRefreshToken = RefreshToken.builder()
                .token("refresh-uuid-token")
                .user(mockUser)
                .expiryDate(Instant.now().plusSeconds(600))
                .build();
    }

    @Test
    @DisplayName("Should register user and return auth response when email is not taken")
    void shouldRegisterSuccessfully() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Dustin", "dustin@fintrack.com", "123456");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(mockUser);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
        when(refreshTokenService.createRefreshToken(any())).thenReturn(mockRefreshToken);

        // Act
        AuthResponseDTO response = authService.register(request);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-uuid-token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when email is already in use")
    void shouldThrowWhenEmailAlreadyExists() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Dustin", "dustin@fintrack.com", "123456");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already in use.");
    }

    @Test
    @DisplayName("Should return auth response when credentials are valid")
    void shouldLoginSuccessfully() {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO("dustin@fintrack.com", "123456");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
        when(refreshTokenService.createRefreshToken(any())).thenReturn(mockRefreshToken);

        // Act
        AuthResponseDTO response = authService.login(request);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        verify(authenticationManager).authenticate(any());
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when credentials are invalid")
    void shouldThrowOnInvalidCredentials() {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO("dustin@fintrack.com", "wrong");
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("Should return new token pair when refresh token is valid")
    void shouldRefreshTokenSuccessfully() {
        // Arrange
        when(refreshTokenService.findByToken("refresh-uuid-token")).thenReturn(mockRefreshToken);
        when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);
        when(jwtService.generateToken(any())).thenReturn("new-jwt-token");
        when(refreshTokenService.createRefreshToken(any())).thenReturn(mockRefreshToken);

        // Act
        AuthResponseDTO response = authService.refreshToken("refresh-uuid-token");

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("new-jwt-token");
        verify(refreshTokenService).deleteByUser(mockUser);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when refresh token does not exist")
    void shouldThrowWhenRefreshTokenNotFound() {
        // Arrange
        when(refreshTokenService.findByToken("invalid")).thenThrow(
                new com.dustin.fintrack.controller.exception.ResourceNotFoundException("Refresh token not found.")
        );

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("invalid"))
                .hasMessageContaining("Refresh token not found.");
    }
}