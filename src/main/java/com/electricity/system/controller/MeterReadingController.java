package com.electricity.system.controller;

import com.electricity.system.model.MeterReading;
import com.electricity.system.model.Customer;
import com.electricity.system.service.MeterReadingService;
import com.electricity.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/readings")
public class MeterReadingController {

    @Autowired
    private MeterReadingService meterReadingService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String allReadings(Model model) {
        model.addAttribute("readings", meterReadingService.getAll());
        return "reading-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("reading", new MeterReading());
        model.addAttribute("customers", customerService.getAll());
        return "reading-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("reading") MeterReading reading, @RequestParam("customerId") Long customerId) {
        Customer customer = customerService.getById(customerId);
        reading.setCustomer(customer);
        reading.setReadingDate(LocalDate.now());

        int units = reading.getUnitsConsumed();
        double amount = 0;

        if (units <= 100) {
            amount = units * 5;
        } else if (units <= 200) {
            amount = (100 * 5) + ((units - 100) * 8);
        } else {
            amount = (100 * 5) + (100 * 8) + ((units - 200) * 10);
        }

        reading.setTotalAmount(amount);
        reading.setStatus("Unpaid");

        meterReadingService.save(reading);
        return "redirect:/readings";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        meterReadingService.delete(id);
        return "redirect:/readings";
    }

    @GetMapping("/pay/{id}")
    public String markAsPaid(@PathVariable Long id) {
        MeterReading reading = meterReadingService.getById(id);
        System.out.println(">>>>>>>>>>>>> :: "+reading);
        if (reading != null) {
            reading.setStatus("Paid");
            System.out.println("updating :: "+ reading.getId());
            System.out.println("updating :: "+ reading.getTotalAmount());
            System.out.println("updating :: "+ reading.getReadingDate());
            System.out.println("updating :: "+ reading.getStatus());
            meterReadingService.save(reading);
        }
        return "redirect:/readings";
    }
}
