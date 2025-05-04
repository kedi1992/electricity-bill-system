package com.electricity.system.controller;

import com.electricity.system.model.BillPayment;
import com.electricity.system.model.Customer;
import com.electricity.system.model.MeterReading;
import com.electricity.system.repository.BillPaymentRepository;
import com.electricity.system.repository.MeterReadingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class CustomerPaymentController {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @PostMapping("/customer/pay")
    public String submitPayment(@RequestParam Long readingId,
                                 @RequestParam String paymentMode,
                                 @RequestParam(required = false) Double amountPaid,
                                 @RequestParam String transactionId,
                                 HttpSession session) {

        Customer customer = (Customer) session.getAttribute("loggedInUser");

        if (customer == null || !"SUCCESS".equalsIgnoreCase(customer.getActivationStatus())) {
            return "redirect:/login";
        }

        MeterReading reading = meterReadingRepository.findById(readingId).orElse(null);
        if (reading == null) return "redirect:/customer/readings";

        double amount = "FULL".equalsIgnoreCase(paymentMode) ? reading.getTotalDue() : amountPaid;

        BillPayment payment = new BillPayment();
        payment.setCustomer(customer);
        payment.setReading(reading);
        payment.setPaymentMode(paymentMode.toUpperCase());
        payment.setAmountPaid(amount);
        payment.setTransactionId(transactionId);
        payment.setStatus("PENDING");
        payment.setRequestedAt(LocalDateTime.now());

        billPaymentRepository.save(payment);

        return "redirect:/customer/readings";
    }
}
