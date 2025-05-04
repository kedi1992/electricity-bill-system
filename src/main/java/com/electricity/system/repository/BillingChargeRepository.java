package com.electricity.system.repository;

import com.electricity.system.model.BillingCharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingChargeRepository extends JpaRepository<BillingCharge, Long> {
    List<BillingCharge> findByActiveTrue();

}
