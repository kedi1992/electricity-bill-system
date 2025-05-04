package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("customers", customerService.getAll());
        return "admin-customer-list";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("customer", new Customer());
        return "admin-customer-form";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer, Model model) {
        try{
            customer.setActivationStatus("PENDING");
            customer.setActivationCode("1234");
            customerService.save(customer);
            return "redirect:/admin/customers";
        } catch (RuntimeException ex) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", ex.getMessage());
            return "admin-customer-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return "admin-customer-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return "redirect:/admin/customers";
    }
}
