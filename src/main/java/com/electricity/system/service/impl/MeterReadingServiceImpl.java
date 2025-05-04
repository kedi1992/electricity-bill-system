package com.electricity.system.service.impl;

import com.electricity.system.model.BillingCharge;
import com.electricity.system.model.Customer;
import com.electricity.system.model.MeterReading;
import com.electricity.system.repository.MeterReadingRepository;
import com.electricity.system.service.BillingChargeService;
import com.electricity.system.service.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class MeterReadingServiceImpl implements MeterReadingService {

    @Autowired
    private MeterReadingRepository readingRepository;

    @Autowired
    private BillingChargeService billingChargeService;

    @Override
    public MeterReading save(MeterReading reading) {
        int units = reading.getUnitsConsumed();
        double amount = 0;

        if (units <= 100) amount = units * 5;
        else if (units <= 200) amount = (100 * 5) + ((units - 100) * 8);
        else amount = (100 * 5) + (100 * 8) + ((units - 200) * 10);

        reading.setTotalAmount(amount);
        reading.setStatus("Unpaid");
        return readingRepository.save(reading);
    }

    @Override
    public List<MeterReading> getByCustomer(Customer customer) {
        return readingRepository.findByCustomer(customer);
    }

    @Override
    public List<MeterReading> getAll() {
        return readingRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        readingRepository.deleteById(id);
    }

    @Override
    public MeterReading getById(Long id) {
        return readingRepository.findById(id).orElse(null);
    }

    @Autowired
    private MeterReadingRepository meterReadingRepository;


    @Override
    public List<MeterReading> getByCustomerAndMonth(Customer customer, int month, int year) {
        return meterReadingRepository.findByCustomerAndBillingMonthAndBillingYear(customer, month, year);
    }

    private double applyBillingCharges(double baseAmount) {
        List<BillingCharge> charges = billingChargeService.getActiveCharges();
        double finalAmount = baseAmount;

        for (BillingCharge charge : charges) {
            if (charge.getChargeType() == BillingCharge.ChargeType.FIXED) {
                finalAmount += charge.getValue();
            } else if (charge.getChargeType() == BillingCharge.ChargeType.PERCENTAGE) {
                finalAmount += baseAmount * (charge.getValue() / 100);
            }
        }

        return finalAmount;
    }

    @Override
    public List<MeterReading> filterBills(Integer month, Integer year, Long customerId, String status) {
        Specification<MeterReading> spec = Specification.where(null);

        if (month != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("billingMonth"), month));
        }
        if (year != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("billingYear"), year));
        }
        if (customerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("customer").get("id"), customerId));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return meterReadingRepository.findAll(spec);
    }

    @Override
    public Page<MeterReading> filterBillsPaginated(Integer month, Integer year, Long customerId, String status, int page, int size) {
        Specification<MeterReading> spec = Specification.where(null);

        if (month != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("billingMonth"), month));
        }
        if (year != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("billingYear"), year));
        }
        if (customerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("customer").get("id"), customerId));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "readingDate"));
        return meterReadingRepository.findAll(spec, pageable);
    }

    // @Override
    // public MeterReading getById(Long id) {
    //     return meterReadingRepository.findById(id).orElse(null);
    // }



}
