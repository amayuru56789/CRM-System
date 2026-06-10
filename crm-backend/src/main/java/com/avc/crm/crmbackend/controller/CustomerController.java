package com.avc.crm.crmbackend.controller;

import com.avc.crm.crmbackend.common.ApiResponse;
import com.avc.crm.crmbackend.common.PageResponse;
import com.avc.crm.crmbackend.dto.request.CustomerRequest;
import com.avc.crm.crmbackend.dto.response.CustomerResponse;
import com.avc.crm.crmbackend.service.CustomerService;
import com.avc.crm.crmbackend.util.enums.CustomerStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author amayuru_i
 * @project crm-backend
 */

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        return ResponseEntity.ok(ApiResponse.success(
                customerService.getAll(
                        search, status, assignedTo, page, size, sortBy)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(customerService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Customer created successfully",
                        customerService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Customer updated successfully",
                customerService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success("Customer deleted successfully"));
    }

}
