package com.dustin.fintrack.repository;

import com.dustin.fintrack.model.RefreshToken;
import com.dustin.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositório para operações de banco do RefreshToken.
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}