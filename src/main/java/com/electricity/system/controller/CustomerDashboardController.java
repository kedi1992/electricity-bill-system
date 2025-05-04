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

    // @GetMapping("/customer/consumption-graph")
    // public String viewUsageGraph(Model model, Principal principal) {
    //     String meterNumber = principal.getName();
    //     Customer customer = customerService.getByMeterNumber(meterNumber);

    //     List<Object[]> usageData = readingService.getLast12MonthsUsage(customer.getId());

    //     List<String> labels = new ArrayList<>();
    //     List<Integer> units = new ArrayList<>();

    //     for (Object[] row : usageData) {
    //         labels.add((String) row[0]);       // Example: "Apr 2025"
    //         units.add((Integer) row[1]);       // Example: 234
    //     }

    //     model.addAttribute("labels", labels);
    //     model.addAttribute("units", units);

    //     return "customer/consumption-graph";
    // }
}
