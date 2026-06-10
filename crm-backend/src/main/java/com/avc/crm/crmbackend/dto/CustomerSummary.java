package com.avc.crm.crmbackend.dto;

import com.avc.crm.crmbackend.util.enums.CustomerStatus;
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
public class CustomerSummary {
    private Long id;
    private String fullName;
    private String email;
    private String company;
    private CustomerStatus status;
}
