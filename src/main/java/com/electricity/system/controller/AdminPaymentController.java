package com.electricity.system.controller;

import com.electricity.system.model.BillPayment;
import com.electricity.system.model.MeterReading;
import com.electricity.system.repository.BillPaymentRepository;
import com.electricity.system.repository.MeterReadingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @GetMapping("/pending")
    public String viewPendingPayments(Model model) {
        List<BillPayment> pendingPayments = billPaymentRepository.findByStatus("PENDING");
        model.addAttribute("pendingPayments", pendingPayments);
        return "admin-pending-payments";
    }

    @PostMapping("/confirm/{id}")
    @Transactional
    public String confirmPayment(@PathVariable Long id) {
        BillPayment payment = billPaymentRepository.findById(id).orElse(null);
        if (payment == null) return "redirect:/admin/payments/pending";

        payment.setStatus("CONFIRMED");
        payment.setConfirmedAt(LocalDateTime.now());
        billPaymentRepository.save(payment);

        MeterReading reading = payment.getReading();
        reading.setAmountPaid(reading.getAmountPaid() + payment.getAmountPaid());
        if (reading.getAmountPaid() >= reading.getTotalAmount()) {
            reading.setStatus("PAID");
            reading.setTotalDue(0.0);
        } else {
            reading.setStatus("PARTIAL");
            reading.setTotalDue(reading.getTotalAmount() - reading.getAmountPaid());
        }
        meterReadingRepository.save(reading);

        return "redirect:/admin/payments/pending";
    }
}
