package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.model.MeterReading;
import com.electricity.system.model.User;
import com.electricity.system.service.CustomerService;
import com.electricity.system.service.MeterReadingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserMeterReadingController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MeterReadingService meterReadingService;

    @GetMapping("/user/readings")
    public String viewUserReadings(@RequestParam(value = "month", required = false) Integer month,
                                   @RequestParam(value = "year", required = false) Integer year,
                                   HttpSession session,
                                   Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Customer customer = customerService.getAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(user.getEmail()))
                .findFirst().orElse(null);

        if (customer == null) {
            model.addAttribute("error", "Customer profile not found.");
            return "user-readings";
        }

        List<MeterReading> allReadings = meterReadingService.getByCustomer(customer);

        // Apply filter if provided
        List<MeterReading> filteredReadings = allReadings;
        if (month != null && year != null) {
            filteredReadings = allReadings.stream()
                    .filter(r -> r.getBillingMonth() == month && r.getBillingYear() == year)
                    .collect(Collectors.toList());
        }

        // Calculate total unpaid and current bill
        double unpaid = allReadings.stream()
                .filter(r -> !"PAID".equalsIgnoreCase(r.getStatus()))
                .mapToDouble(r -> r.getTotalAmount() - r.getAmountPaid())
                .sum();

        model.addAttribute("customer", customer);
        model.addAttribute("readings", filteredReadings);
        model.addAttribute("totalDue", unpaid);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "user-readings";
    }
}
