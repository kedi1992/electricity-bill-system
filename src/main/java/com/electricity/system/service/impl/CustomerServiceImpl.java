package com.electricity.system.service.impl;

import com.electricity.system.model.Customer;
import com.electricity.system.repository.CustomerRepository;
import com.electricity.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private String generateMeterNumber() {
        long count = customerRepository.count() + 1;
        return String.format("MT%05d", count); // e.g., MT00001
    }

//     @Override
//     public Customer save(Customer customer) {
//     if (customer.getId() == null) {
//         customer.setActivationStatus("PENDING");
//         customer.setActivationCode("1234");

//         if (customer.getMeterNumber() == null || customer.getMeterNumber().isEmpty()) {
//             customer.setMeterNumber(generateMeterNumber());
//         }
//     }

//     return customerRepository.save(customer);
// }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            // CREATE mode - check for duplicate mobile/email
            if (customerRepository.findByMobile(customer.getMobile()) != null) {
                throw new RuntimeException("Mobile number already exists.");
            }
            if (customerRepository.findByEmail(customer.getEmail()) != null) {
                throw new RuntimeException("Email already exists.");
            }
    
            // Generate meter number
            long count = customerRepository.count();
            customer.setMeterNumber(String.format("MT%05d", count + 1));
        } else {
            // UPDATE mode - allow updates without modifying meter/mobile/email
            Customer existing = customerRepository.findById(customer.getId()).orElse(null);
            if (existing != null) {
                existing.setFullName(customer.getFullName());
                existing.setAddress(customer.getAddress());
                existing.setCity(customer.getCity());
                existing.setRole(customer.getRole());
                return customerRepository.save(existing);
            }
        }
    
        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    // private String generateMeterNumber() {
    //     long count = customerRepository.count() + 1;
    //     return String.format("MT%05d", count); // e.g., MT00001
    // }

    @Override
    public Customer getByMeterNumber(String meterNumber) {
        return customerRepository.findByMeterNumber(meterNumber);
    }
}
