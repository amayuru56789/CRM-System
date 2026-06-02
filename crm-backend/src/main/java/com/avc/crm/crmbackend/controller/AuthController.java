package com.avc.crm.crmbackend.controller;

import com.avc.crm.crmbackend.common.ApiResponse;
import com.avc.crm.crmbackend.dto.request.LoginRequest;
import com.avc.crm.crmbackend.dto.request.RefreshRequest;
import com.avc.crm.crmbackend.dto.request.RegisterRequest;
import com.avc.crm.crmbackend.dto.response.AuthResponseDTO;
import com.avc.crm.crmbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(
                ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> refresh(
            @Valid @RequestBody RefreshRequest request) {
        AuthResponseDTO response = authService.refresh(request);
        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed", response));
    }
}
