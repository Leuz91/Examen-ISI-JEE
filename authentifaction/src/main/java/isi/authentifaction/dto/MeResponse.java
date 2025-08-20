package isi.authentifaction.dto;

import java.util.Set;

public record MeResponse(
        Long id, String username, String email, Set<String> roles
) {}