package com.avc.crm.crmbackend.dto.response;

import com.avc.crm.crmbackend.util.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private Long userId;
    private String name;
    private String email;
    private UserRole role;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

}
