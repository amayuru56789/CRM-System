package com.avc.crm.crmbackend.dto.request;

import com.avc.crm.crmbackend.util.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 50)
    private String password;
    @NotNull
    private UserRole role;
}
