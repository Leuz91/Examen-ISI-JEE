package isi.authentifaction.dto;

import java.time.Instant;

public record TokenResponse(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String tokenType // "Bearer"
) {}
