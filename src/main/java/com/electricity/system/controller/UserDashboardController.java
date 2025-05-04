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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserDashboardController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MeterReadingService meterReadingService;

    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // Fetch customer details by user
        Customer customer = customerService.getAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(user.getEmail()))
                .findFirst().orElse(null);

        if (customer == null) {
            model.addAttribute("error", "Customer profile not found.");
            return "user-dashboard";
        }

        // All readings
        List<MeterReading> allReadings = meterReadingService.getByCustomer(customer);

        // Current month bill
        LocalDate now = LocalDate.now();
        List<MeterReading> currentMonthReadings = allReadings.stream()
                .filter(r -> r.getReadingDate().getMonth() == now.getMonth() &&
                             r.getReadingDate().getYear() == now.getYear())
                .collect(Collectors.toList());

        model.addAttribute("customer", customer);
        model.addAttribute("allReadings", allReadings);
        model.addAttribute("currentMonthReadings", currentMonthReadings);

        return "user-dashboard";
    }
}
