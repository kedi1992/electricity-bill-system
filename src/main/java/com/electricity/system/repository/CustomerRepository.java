package com.electricity.system.repository;

import com.electricity.system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByMeterNumber(String meterNumber);
}
