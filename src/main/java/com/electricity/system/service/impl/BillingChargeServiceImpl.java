package com.electricity.system.service.impl;

import com.electricity.system.model.BillingCharge;
import com.electricity.system.repository.BillingChargeRepository;
import com.electricity.system.service.BillingChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingChargeServiceImpl implements BillingChargeService {

    @Autowired
    private BillingChargeRepository billingChargeRepository;

    @Override
    public List<BillingCharge> getActiveCharges() {
        return billingChargeRepository.findByActiveTrue();
    }
}
