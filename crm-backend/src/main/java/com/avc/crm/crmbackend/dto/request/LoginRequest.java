package com.avc.crm.crmbackend.dto.request;

import lombok.Data;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Data
public class LoginRequest {
//    @NotBlank @Email
    private String email;
//    @NotBlank @Size(min = 6)
    private String password;
}
