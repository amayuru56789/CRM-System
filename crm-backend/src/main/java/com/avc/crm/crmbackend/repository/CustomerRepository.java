package com.avc.crm.crmbackend.repository;

import com.avc.crm.crmbackend.entity.Customer;
import com.avc.crm.crmbackend.util.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author amayuru_i
 * @project crm-backend
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Search by name, email, company
    @Query("""
        SELECT c FROM Customer c
        WHERE (:search IS NULL OR
               LOWER(c.firstName) LIKE LOWER(CONCAT('%',:search,'%')) OR
               LOWER(c.lastName)  LIKE LOWER(CONCAT('%',:search,'%')) OR
               LOWER(c.email)     LIKE LOWER(CONCAT('%',:search,'%')) OR
               LOWER(c.company)   LIKE LOWER(CONCAT('%',:search,'%')))
        AND   (:status IS NULL OR c.status = :status)
        AND   (:assignedTo IS NULL OR c.assignedTo.id = :assignedTo)
        """)
    Page<Customer> search(
            @Param("search") String search,
            @Param("status") CustomerStatus status,
            @Param("assignedTo") Long assignedTo,
            Pageable pageable);

    boolean existsByEmail(String email);

    List<Customer> findByAssignedToId(Long userId);

    // Count by status for dashboard
    long countByStatus(CustomerStatus status);
}
