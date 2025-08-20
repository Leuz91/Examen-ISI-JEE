package isi.authentifaction.service;

import isi.authentifaction.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

// JwtService.java
@Service
@RequiredArgsConstructor
public class JwtService {
    private final Environment env;

    private Key signingKey() {
        // secret en Base64 ou brut; ici on le dérive simplement
        String secret = env.getProperty("security.jwt.secret");
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey()).build()
                .parseClaimsJws(token).getBody();
        var roles = (Collection<String>) claims.getOrDefault("roles", List.of("USER"));
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
    }
}
