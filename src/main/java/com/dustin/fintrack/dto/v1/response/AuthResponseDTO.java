package com.dustin.fintrack.dto.v1.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String accessToken;

    private String refreshToken;

    private UserResponseDTO user;
}