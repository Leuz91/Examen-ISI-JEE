package isi.authentifaction.controller;

import isi.authentifaction.dto.*;
import isi.authentifaction.model.User;
import isi.authentifaction.repositories.UserRepository;
import isi.authentifaction.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository users;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/auth/token")
    public ResponseEntity<TokenResponse> token(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        return ResponseEntity.ok(authService.refresh(req));
    }

    @GetMapping("/auth/me")
    public MeResponse me(Authentication auth) {
        User u = users.findByUsername(auth.getName()).orElseThrow();
        return new MeResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRoles());
    }
}
