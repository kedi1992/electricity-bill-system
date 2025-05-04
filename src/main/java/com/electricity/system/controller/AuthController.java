package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.model.User;
import com.electricity.system.repository.CustomerRepository;
import com.electricity.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,  // this is meter number
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
    
        Customer customer = customerRepository.findByMeterNumber(username);
        
    
        if (customer != null &&
            "SUCCESS".equalsIgnoreCase(customer.getActivationStatus()) &&
            customer.getPassword() != null &&
            customer.getPassword().equals(password)) {
    
            session.setAttribute("loggedInUser", customer);
    
            if ("ADMIN".equalsIgnoreCase(customer.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/customer/dashboard";
            }
    
        } else {
            model.addAttribute("error", "Invalid credentials or account not activated.");
            return "login";
        }
    }


    // @GetMapping("/signup")
    // public String signupPage(Model model) {
    //     model.addAttribute("user", new User());
    //     return "signup";
    // }

    // @PostMapping("/signup")
    // public String signup(@ModelAttribute User user) {
    //     user.setRole("USER");
    //     userService.save(user);
    //     return "redirect:/login";
    // }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }
}
