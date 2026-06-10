package com.avc.crm.crmbackend.dto.request;

import com.avc.crm.crmbackend.util.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Data
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 150)
    private String company;

    @Size(max = 100)
    private String industry;

    private CustomerStatus status;
    private String         notes;
    private Long           assignedTo;

}
