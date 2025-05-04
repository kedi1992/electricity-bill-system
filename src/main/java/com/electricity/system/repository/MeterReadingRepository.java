package com.electricity.system.repository;

import com.electricity.system.model.MeterReading;
import com.electricity.system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long>, JpaSpecificationExecutor<MeterReading>  {
    List<MeterReading> findByCustomer(Customer customer);
    List<MeterReading> findByCustomerAndBillingMonthAndBillingYear(Customer customer, int billingMonth, int billingYear);
    List<MeterReading> findByCustomerAndReadingDateBetweenOrderByReadingDateDesc(
    Customer customer,
    LocalDate from,
    LocalDate to
);
     @Query("""
        SELECT CONCAT(mr.billingMonth, '-', mr.billingYear) AS label, mr.unitsConsumed
        FROM MeterReading mr
        WHERE mr.customer.id = :customerId
        ORDER BY mr.billingYear DESC, mr.billingMonth DESC
    """)
    List<Object[]> getUsageHistory(@Param("customerId") Long customerId);
}
