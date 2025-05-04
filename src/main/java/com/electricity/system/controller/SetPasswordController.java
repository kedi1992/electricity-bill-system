package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SetPasswordController {

    @Autowired
    private CustomerRepository customerRepository;

    private static final String STATIC_ACTIVATION_CODE = "1234";

    @GetMapping("/set-password")
    public String showSetPasswordForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "set-password";
    }

    @PostMapping("/set-password")
    public String handleSetPassword(@RequestParam String meterNumber,
                                    @RequestParam String activationCode,
                                    @RequestParam String password,
                                    @RequestParam String confirmPassword,
                                    Model model) {
        Customer customer = customerRepository.findByMeterNumber(meterNumber);

        if (customer == null || !STATIC_ACTIVATION_CODE.equals(activationCode)
                || !"PENDING".equalsIgnoreCase(customer.getActivationStatus())) {
            model.addAttribute("error", "Invalid meter number or activation code.");
            return "set-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "set-password";
        }

        customer.setPassword(password);
        customer.setActivationStatus("SUCCESS");
        customer.setActivationCode(null);
        customerRepository.save(customer);

        model.addAttribute("success", "Password set successfully! Please login.");
        return "redirect:/login";
    }
}
