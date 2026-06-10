package com.avc.crm.crmbackend.service;

import com.avc.crm.crmbackend.common.PageResponse;
import com.avc.crm.crmbackend.dto.request.CustomerRequest;
import com.avc.crm.crmbackend.dto.response.CustomerResponse;
import com.avc.crm.crmbackend.entity.Customer;
import com.avc.crm.crmbackend.util.enums.CustomerStatus;

/**
 * @author amayuru_i
 * @project crm-backend
 */
public interface CustomerService {
    public PageResponse<CustomerResponse> getAll(
            String search, CustomerStatus status,
            Long assignedTo, int page, int size, String sortBy);
    public CustomerResponse getById(Long id);
    public CustomerResponse create(CustomerRequest request);
    public CustomerResponse update(Long id, CustomerRequest request);
    public void delete(Long id);
}
