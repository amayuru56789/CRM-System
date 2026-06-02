package com.avc.crm.crmbackend.service;

import com.avc.crm.crmbackend.dto.request.LoginRequest;
import com.avc.crm.crmbackend.dto.request.RefreshRequest;
import com.avc.crm.crmbackend.dto.request.RegisterRequest;
import com.avc.crm.crmbackend.dto.response.AuthResponseDTO;
import com.avc.crm.crmbackend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author amayuru_i
 * @project crm-backend
 */

public interface AuthService {
    public AuthResponseDTO login(LoginRequest request);
    public AuthResponseDTO register(RegisterRequest request);
    public AuthResponseDTO refresh(RefreshRequest request);
    public AuthResponseDTO buildAuthResponse(User user);
}
