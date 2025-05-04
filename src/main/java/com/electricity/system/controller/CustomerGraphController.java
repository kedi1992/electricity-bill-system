package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.service.CustomerService;
import com.electricity.system.service.MeterReadingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CustomerGraphController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MeterReadingService meterReadingService;

    @GetMapping("/customer/consumption-graph")
    public String showGraph(Model model) {
        String meterNumber = "MT00002";
        Customer customer = customerService.getByMeterNumber(meterNumber);

        List<Object[]> rawData = meterReadingService.getLast12MonthUsage(customer.getId());

        List<String> labels = new ArrayList<>();
        List<Integer> units = new ArrayList<>();

        // Reverse to show oldest month first
        Collections.reverse(rawData);

        for (Object[] row : rawData) {
            String[] parts = ((String) row[0]).split("-");
            String month = getMonthName(Integer.parseInt(parts[0]));
            String year = parts[1];
            labels.add(month + " " + year);
            units.add(((Number) row[1]).intValue());
        }

        model.addAttribute("labels", labels);
        model.addAttribute("units", units);
        return "customer/consumption-graph";
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 -> "Jan"; case 2 -> "Feb"; case 3 -> "Mar"; case 4 -> "Apr";
            case 5 -> "May"; case 6 -> "Jun"; case 7 -> "Jul"; case 8 -> "Aug";
            case 9 -> "Sep"; case 10 -> "Oct"; case 11 -> "Nov"; case 12 -> "Dec";
            default -> "N/A";
        };
    }
}
