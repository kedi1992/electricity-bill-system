package com.electricity.system.service;

import com.electricity.system.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);
    Customer getById(Long id);
    Customer getByMeterNumber(String meterNumber);
    List<Customer> getAll();
    void deleteById(Long id);
}
