package com.avc.crm.crmbackend.service.impl;

import com.avc.crm.crmbackend.common.PageResponse;
import com.avc.crm.crmbackend.common.exception.BusinessException;
import com.avc.crm.crmbackend.common.exception.DuplicateResourceException;
import com.avc.crm.crmbackend.common.exception.ResourceNotFoundException;
import com.avc.crm.crmbackend.dto.request.CustomerRequest;
import com.avc.crm.crmbackend.dto.response.CustomerResponse;
import com.avc.crm.crmbackend.entity.Customer;
import com.avc.crm.crmbackend.entity.User;
import com.avc.crm.crmbackend.repository.CustomerRepository;
import com.avc.crm.crmbackend.repository.UserRepository;
import com.avc.crm.crmbackend.service.CustomerService;
import com.avc.crm.crmbackend.util.enums.CustomerStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author amayuru_i
 * @project crm-backend
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse<CustomerResponse> getAll(String search, CustomerStatus status, Long assignedTo, int page, int size, String sortBy) {

        try {

            log.debug("Fetching customers — search:{} status:{} page:{}",
                    search, status, page);

            // ── Get all (paginated + search) ────────────────────────
            Pageable pageable = PageRequest.of(
                    page, size, Sort.by(sortBy).descending());

            Page<Customer> result = customerRepository.search(
                    search, status, assignedTo, pageable);

            return PageResponse.<CustomerResponse>builder()
                    .content(result.getContent().stream()
                            .map(this::toResponse).toList())
                    .page(result.getNumber())
                    .size(result.getSize())
                    .totalElements(result.getTotalElements())
                    .totalPages(result.getTotalPages())
                    .last(result.isLast())
                    .build();
        } catch (DataAccessException ex) {
            log.error("DB error fetching customers: {}", ex.getMessage());
            throw new BusinessException(
                    "Failed to fetch customers. Please try again.",
                    "CUSTOMER_FETCH_ERROR");
        }
    }

    @Override
    public CustomerResponse getById(Long id) {
        try {
            log.debug("Fetching customer id:{}", id);
            // ── Get by ID ─────────────────────────────────────────
            return toResponse(findById(id));
        } catch (ResourceNotFoundException ex) {
            throw ex;  // rethrow — already handled
        } catch (DataAccessException ex) {
            log.error("DB error fetching customer {}: {}",
                    id, ex.getMessage());
            throw new BusinessException(
                    "Failed to fetch customer. Please try again.",
                    "CUSTOMER_FETCH_ERROR");
        }
    }

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {

        try {
            log.debug("Creating customer: {}", request.getEmail());
            // ── Create ──────────────────────────────────────────────
            if (request.getEmail() != null &&
                    customerRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException(
                        "Email already exists: " + request.getEmail());
            }

            Customer customer = Customer.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .company(request.getCompany())
                    .industry(request.getIndustry())
                    .status(request.getStatus() != null
                            ? request.getStatus() : CustomerStatus.PROSPECT)
                    .notes(request.getNotes())
                    .assignedTo(resolveUser(request.getAssignedTo()))
                    .build();

            return toResponse(customerRepository.save(customer));

        } catch (DuplicateResourceException
                 | ResourceNotFoundException ex) {
            throw ex;  // rethrow known exceptions
        } catch (DataAccessException ex) {
            log.error("DB error creating customer: {}",
                    ex.getMessage());
            throw new BusinessException(
                    "Failed to create customer. Please try again.",
                    "CUSTOMER_CREATE_ERROR");
        } catch (Exception ex) {
            log.error("Unexpected error creating customer: {}",
                    ex.getMessage(), ex);
            throw new BusinessException(
                    "An unexpected error occurred while creating customer.",
                    "CUSTOMER_CREATE_ERROR");
        }
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {

        try {
            log.debug("Updating customer id:{}", id);

            // ── Update ──────────────────────────────────────────────
            Customer customer = findById(id);

            if (request.getEmail() != null &&
                    !request.getEmail().equals(customer.getEmail()) &&
                    customerRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException(
                        "Email already exists: " + request.getEmail());
            }

            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            customer.setCompany(request.getCompany());
            customer.setIndustry(request.getIndustry());
            customer.setNotes(request.getNotes());

            if (request.getStatus() != null)
                customer.setStatus(request.getStatus());

            customer.setAssignedTo(resolveUser(request.getAssignedTo()));

            return toResponse(customerRepository.save(customer));

        } catch (DuplicateResourceException
                 | ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            log.error("DB error updating customer {}: {}",
                    id, ex.getMessage());
            throw new BusinessException(
                    "Failed to update customer. Please try again.",
                    "CUSTOMER_UPDATE_ERROR");
        } catch (Exception ex) {
            log.error("Unexpected error updating customer {}: {}",
                    id, ex.getMessage(), ex);
            throw new BusinessException(
                    "An unexpected error occurred while updating customer.",
                    "CUSTOMER_UPDATE_ERROR");
        }

    }

    @Override
    @Transactional
    public void delete(Long id) {

        try {
            log.debug("Deleting customer id:{}", id);

            // ── Delete ──────────────────────────────────────────────
            if (!customerRepository.existsById(id))
                throw new EntityNotFoundException("Customer not found: " + id);
            customerRepository.deleteById(id);

            log.info("Customer deleted — id:{}", id);

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            log.error("DB error deleting customer {}: {}",
                    id, ex.getMessage());
            throw new BusinessException(
                    "Failed to delete customer. It may be referenced "
                            + "by leads or deals.",
                    "CUSTOMER_DELETE_ERROR");
        } catch (Exception ex) {
            log.error("Unexpected error deleting customer {}: {}",
                    id, ex.getMessage(), ex);
            throw new BusinessException(
                    "An unexpected error occurred while deleting customer.",
                    "CUSTOMER_DELETE_ERROR");
        }
    }

    // ── Private helpers ─────────────────────────────────────
    private Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found: " + id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: " + userId));
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .fullName(c.getFullName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .company(c.getCompany())
                .industry(c.getIndustry())
                .status(c.getStatus())
                .notes(c.getNotes())
                .assignedToId(c.getAssignedTo() != null
                        ? c.getAssignedTo().getId() : null)
                .assignedToName(c.getAssignedTo() != null
                        ? c.getAssignedTo().getName() : null)
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
