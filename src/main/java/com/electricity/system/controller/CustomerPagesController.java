package com.electricity.system.controller;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.electricity.system.model.Customer;
import com.electricity.system.model.MeterReading;
import com.electricity.system.repository.BillPaymentRepository;
import com.electricity.system.repository.MeterReadingRepository;
import java.util.List;
import java.util.Set;
import org.springframework.ui.Model;


@Controller
public class CustomerPagesController {


    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @GetMapping("/customer/profile")
    public String viewProfile(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("loggedInUser");

        if (customer == null || !"SUCCESS".equalsIgnoreCase(customer.getActivationStatus())) {
            return "redirect:/login";
        }

        model.addAttribute("customer", customer);
        return "customer/profile";
    }

    @GetMapping("/customer/readings")
    public String viewReadingHistory(HttpSession session,
                                     Model model,
                                     @RequestParam(value = "from", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                     @RequestParam(value = "to", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Customer customer = (Customer) session.getAttribute("loggedInUser");

        if (customer == null || !"SUCCESS".equalsIgnoreCase(customer.getActivationStatus())) {
            return "redirect:/login";
        }

        LocalDate today = LocalDate.now();
        if (toDate == null) toDate = today;
        if (fromDate == null) fromDate = toDate.minusMonths(12);

        List<MeterReading> readings = meterReadingRepository
                .findByCustomerAndReadingDateBetweenOrderByReadingDateDesc(customer, fromDate, toDate);

        Set<Long> pendingReadingIds = billPaymentRepository.findPendingReadingIdsForCustomer(customer.getId());

        model.addAttribute("readings", readings);
        model.addAttribute("pendingReadingIds", pendingReadingIds);
        model.addAttribute("from", fromDate);
        model.addAttribute("to", toDate);
        return "customer/readings";
    }

    @GetMapping("/customer/bill/current")
    public String currentBill() {
        return "customer/bill-current";
    }

    @GetMapping("/customer/bill/print")
    public String printBill() {
        return "customer/bill-print";
    }

    @GetMapping("/customer/bill/reminders")
    public String billReminders() {
        return "customer/bill-reminders";
    }

    @GetMapping("/customer/bill/filter")
    public String billFilter() {
        return "customer/bill-filter";
    }

    @GetMapping("/customer/readings/submit")
    public String submitReading() {
        return "customer/submit-reading";
    }

    @GetMapping("/customer/help")
    public String help() {
        return "customer/help";
    }

    @GetMapping("/customer/consumption-graph")
    public String graph() {
        return "customer/consumption-graph";
    }
}
