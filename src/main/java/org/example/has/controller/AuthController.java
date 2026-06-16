package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.LoginRequest;
import org.example.has.dto.request.RegisterRequest;
import org.example.has.dto.response.ApiResponse;
import org.example.has.dto.response.AuthResponse;
import org.example.has.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller handling user authentication. Public endpoints (no JWT required).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "API for login and registration")
public class AuthController {

    private final AuthService authService;

    /**
 * Registers a new patient account.
 *
 * @param request the registration details
 * @return AuthResponse containing JWT token and basic user info
 */
    @PostMapping("/register")
    @Operation(summary = "Register account",
            description = "Register a new patient account. returns JWT token để login ngay.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    /**
 * Authenticates username/password credentials.
 *
 * @param request the login credentials
 * @return AuthResponse containing JWT token (24h validity) and basic user info
 */
    @PostMapping("/login")
    @Operation(summary = "Login",
            description = "Login bằng username/password. returns JWT token (24h).")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }
}
