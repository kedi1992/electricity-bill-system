package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerDashboardController {

    @GetMapping("/customer/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("loggedInUser");

        if (customer == null || !"SUCCESS".equalsIgnoreCase(customer.getActivationStatus())) {
            return "redirect:/login";
        }

        model.addAttribute("customer", customer);
        return "customer-dashboard";
    }
}
