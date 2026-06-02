package com.avc.crm.crmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Data
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}
