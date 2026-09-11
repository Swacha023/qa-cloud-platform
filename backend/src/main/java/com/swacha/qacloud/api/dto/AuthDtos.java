package com.swacha.qacloud.api.dto;

public final class AuthDtos {
    private AuthDtos() {}
    public record LoginRequest(String email, String password) {}
    public record LoginResponse(String token, String email, String displayName) {}
}
