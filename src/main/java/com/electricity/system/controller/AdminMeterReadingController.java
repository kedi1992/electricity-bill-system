package com.electricity.system.controller;

import com.electricity.system.model.Customer;
import com.electricity.system.model.MeterReading;
import com.electricity.system.model.BillingCharge;
import com.electricity.system.service.BillingChargeService;
import com.electricity.system.service.CustomerService;
import com.electricity.system.service.MeterReadingService;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMeterReadingController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MeterReadingService meterReadingService;

    @Autowired
    private BillingChargeService billingChargeService;

    // Show add meter reading form
    @GetMapping("/add-reading")
    public String showReadingForm(Model model, HttpSession session) {
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("reading", new MeterReading());
        return "admin-meter-form";
    }

    // Save meter reading
    @PostMapping("/save-reading")
    public String saveReading(@ModelAttribute MeterReading reading, @RequestParam Long customerId, @RequestParam String billingPeriod) {
        Customer customer = customerService.getById(customerId);

        YearMonth ym = YearMonth.parse(billingPeriod); // Format: YYYY-MM
        System.out.println(">>>>>>>>>>>>>");
        System.out.println(ym.getMonthValue());
        System.out.println("rrrrrrrrrrrrrrrrrrrrr");

        reading.setBillingYear(ym.getYear());
        reading.setBillingMonth(ym.getMonthValue());
        reading.setReadingDate(ym.atDay(1)); // Set to 1st of the month
        

        reading.setCustomer(customer);
        // reading.setReadingDate(LocalDate.now());
        // reading.setReadingDate(LocalDate.of(reading.getBillingYear(), reading.getBillingMonth(), 1));
        // reading.setReadingDate(LocalDate.of(reading.getBillingYear(), reading.getBillingMonth(), 1));

        // Fetch last reading
        List<MeterReading> history = meterReadingService.getByCustomer(customer);
        MeterReading last = history.stream().reduce((first, second) -> second).orElse(null);

        double prevReading = last != null ? last.getCurrReading() : 0.0;
        reading.setPrevReading(prevReading);

        double currReading = reading.getCurrReading();
        int units = (int) (currReading - prevReading);
        reading.setUnitsConsumed(units);

        double rate = 5.0; // default flat rate (can also come from config)
        reading.setRatePerUnit(rate);

        double baseAmount = units * rate;
        double totalAmount = applyBillingCharges(baseAmount);

        reading.setTotalAmount(totalAmount);
        reading.setAmountPaid(0.0);
        reading.setAdvanceAmount(0.0);
        reading.setAdjustedAmount(0.0);
        reading.setTotalDue(totalAmount);
        reading.setStatus("UNPAID");

        meterReadingService.save(reading);
        return "redirect:/admin/view-bills";
    }

    // Apply billing charges dynamically
    private double applyBillingCharges(double baseAmount) {
        List<BillingCharge> charges = billingChargeService.getActiveCharges();
        double finalAmount = baseAmount;

        for (BillingCharge charge : charges) {
            if (charge.getChargeType() == BillingCharge.ChargeType.FIXED) {
                finalAmount += charge.getValue();
            } else if (charge.getChargeType() == BillingCharge.ChargeType.PERCENTAGE) {
                finalAmount += baseAmount * (charge.getValue() / 100);
            }
        }

        return finalAmount;
    }

    @GetMapping("/view-bills")
    public String viewBills(@RequestParam(required = false) Integer month,
                        @RequestParam(required = false) Integer year,
                        @RequestParam(required = false) Long customerId,
                        @RequestParam(required = false) String status,
                        @RequestParam(defaultValue = "0") int page,
                        Model model,
                        HttpSession session) {

    Customer user = (Customer) session.getAttribute("loggedInUser");
    if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
        return "redirect:/login";
    }

    Page<MeterReading> billsPage = meterReadingService.filterBillsPaginated(month, year, customerId, status, page, 10);
    List<Customer> customers = customerService.getAll();

    model.addAttribute("bills", billsPage.getContent());
    model.addAttribute("customers", customers);
    model.addAttribute("month", month);
    model.addAttribute("year", year);
    model.addAttribute("customerId", customerId);
    model.addAttribute("status", status);
    model.addAttribute("totalPages", billsPage.getTotalPages());
    model.addAttribute("currentPage", page);

        return "view-bills";
    }

    

}
