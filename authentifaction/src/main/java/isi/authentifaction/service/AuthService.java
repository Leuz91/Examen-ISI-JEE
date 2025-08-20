package isi.authentifaction.service;

import isi.authentifaction.dto.AuthRequest;
import isi.authentifaction.dto.RefreshRequest;
import isi.authentifaction.dto.RegisterRequest;
import isi.authentifaction.dto.TokenResponse;
import isi.authentifaction.model.RefreshToken;
import isi.authentifaction.model.User;
import isi.authentifaction.repositories.RefreshTokenRepository;
import isi.authentifaction.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    @Value("${security.jwt.access-expiration-minutes}") private long accessMinutes;
    @Value("${security.jwt.refresh-expiration-days}")  private long refreshDays;

    @Transactional
    public TokenResponse register(RegisterRequest req) {
        if (users.existsByUsername(req.username()) || users.existsByEmail(req.email()))
            throw new IllegalArgumentException("Username or email already used");

        User u = users.save(User.builder()
                .username(req.username())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .roles(Set.of("USER"))
                .enabled(true)
                .build());

        return issueTokensFor(u); // auto-login après signup (optionnel)
    }

    @Transactional
    public TokenResponse login(AuthRequest req) {
        var authReq = UsernamePasswordAuthenticationToken.unauthenticated(req.username(), req.password());
        authManager.authenticate(authReq); // lève une exception si échec
        User u = users.findByUsername(req.username()).orElseThrow();
        return issueTokensFor(u);
    }

    @Transactional
    public TokenResponse refresh(RefreshRequest req) {
        RefreshToken rt = refreshTokens.findByToken(req.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now()))
            throw new BadCredentialsException("Expired or revoked refresh token");

        // rotation
        rt.setRevoked(true);
        refreshTokens.save(rt);

        return issueTokensFor(rt.getUser());
    }

    private TokenResponse issueTokensFor(User u) {
        // access
        Instant accessExp = Instant.now().plus(Duration.ofMinutes(accessMinutes));
        String access = jwt.generateAccessToken(u, Duration.ofMinutes(accessMinutes));

        // refresh (opaque)
        String rawRefresh = UUID.randomUUID().toString().replace("-", "");
        Instant refreshExp = Instant.now().plus(Duration.ofDays(refreshDays));
        refreshTokens.save(RefreshToken.builder()
                .token(rawRefresh).user(u).expiresAt(refreshExp).revoked(false).build());

        return new TokenResponse(access, accessExp, rawRefresh, refreshExp, "Bearer");
    }
}
