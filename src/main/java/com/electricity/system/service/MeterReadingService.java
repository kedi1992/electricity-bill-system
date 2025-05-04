package com.electricity.system.service;

import com.electricity.system.model.MeterReading;
import com.electricity.system.model.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MeterReadingService {
    MeterReading save(MeterReading reading);
    List<MeterReading> getByCustomer(Customer customer);
    List<MeterReading> getAll();
    void delete(Long id);
    MeterReading getById(Long id);
    List<MeterReading> getByCustomerAndMonth(Customer customer, int month, int year);
    List<MeterReading> filterBills(Integer month, Integer year, Long customerId, String status);
    Page<MeterReading> filterBillsPaginated(Integer month, Integer year, Long customerId, String status, int page, int size);


    

    
}
