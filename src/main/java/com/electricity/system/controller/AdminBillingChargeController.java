package com.electricity.system.controller;

import com.electricity.system.model.BillingCharge;
import com.electricity.system.service.BillingChargeService;
import com.electricity.system.repository.BillingChargeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/charges")
public class AdminBillingChargeController {

    @Autowired
    private BillingChargeRepository billingChargeRepository;

    @GetMapping
    public String listCharges(Model model) {
        model.addAttribute("charges", billingChargeRepository.findAll());
        return "charges-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("charge", new BillingCharge());
        model.addAttribute("types", BillingCharge.ChargeType.values());
        return "charges-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<BillingCharge> charge = billingChargeRepository.findById(id);
        charge.ifPresent(c -> model.addAttribute("charge", c));
        model.addAttribute("types", BillingCharge.ChargeType.values());
        return "charges-form";
    }

    @PostMapping("/save")
    public String saveCharge(@ModelAttribute BillingCharge charge) {
        billingChargeRepository.save(charge);
        return "redirect:/admin/charges";
    }

    @GetMapping("/delete/{id}")
    public String deleteCharge(@PathVariable Long id) {
        billingChargeRepository.deleteById(id);
        return "redirect:/admin/charges";
    }
}
