package com.electricity.system.service;

import com.electricity.system.model.BillingCharge;
import java.util.List;

public interface BillingChargeService {
    List<BillingCharge> getActiveCharges();
}