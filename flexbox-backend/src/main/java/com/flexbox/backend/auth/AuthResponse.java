package com.flexbox.backend.auth;

public record AuthResponse(Long userId, String email, String token) {
}
