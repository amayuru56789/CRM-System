package com.avc.crm.crmbackend.service.impl;

import com.avc.crm.crmbackend.auth.JwtUtil;
import com.avc.crm.crmbackend.dto.request.LoginRequest;
import com.avc.crm.crmbackend.dto.request.RefreshRequest;
import com.avc.crm.crmbackend.dto.request.RegisterRequest;
import com.avc.crm.crmbackend.dto.response.AuthResponseDTO;
import com.avc.crm.crmbackend.entity.User;
import com.avc.crm.crmbackend.repository.UserRepository;
import com.avc.crm.crmbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponseDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: "
                    + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponseDTO refresh(RefreshRequest request) {
        String email = jwtUtil.extractEmail(request.getRefreshToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtUtil.isTokenValid(request.getRefreshToken(), email)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponseDTO buildAuthResponse(User user) {
        return AuthResponseDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(jwtUtil.generateToken(user))
                .refreshToken(jwtUtil.generateRefreshToken(user))
                .build();
    }

}
