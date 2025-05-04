package com.electricity.system.repository;

import com.electricity.system.model.BillPayment;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    @Query("SELECT p.reading.id FROM BillPayment p WHERE p.customer.id = :customerId AND p.status = 'PENDING'")
    Set<Long> findPendingReadingIdsForCustomer(@Param("customerId") Long customerId);
    List<BillPayment> findByStatus(String status);

}
