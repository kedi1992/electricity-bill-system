package com.electricity.system.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "meter_readings")
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private int billingMonth;
    private int billingYear;
    private int unitsConsumed;
    private double ratePerUnit;
    private double totalAmount;
    private double amountPaid;
    private String status; // PAID, UNPAID, PARTIAL
    private LocalDate readingDate;
    private LocalDate lastPaymentDate;
    private double advanceAmount;
    private double adjustedAmount;
    private double currReading;
    private double prevReading;
    public double getCurrReading() {
        return currReading;
    }
    public void setCurrReading(double currReading) {
        this.currReading = currReading;
    }
    public double getPrevReading() {
        return prevReading;
    }
    public void setPrevReading(double prevReading) {
        this.prevReading = prevReading;
    }
    private double totalDue;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public int getBillingMonth() { return billingMonth; }
    public void setBillingMonth(int billingMonth) { this.billingMonth = billingMonth; }

    public int getBillingYear() { return billingYear; }
    public void setBillingYear(int billingYear) { this.billingYear = billingYear; }

    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public double getRatePerUnit() { return ratePerUnit; }
    public void setRatePerUnit(double ratePerUnit) { this.ratePerUnit = ratePerUnit; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }

    public double getAdvanceAmount() { return advanceAmount; }
    public void setAdvanceAmount(double advanceAmount) { this.advanceAmount = advanceAmount; }

    public double getAdjustedAmount() { return adjustedAmount; }
    public void setAdjustedAmount(double adjustedAmount) { this.adjustedAmount = adjustedAmount; }

    public double getTotalDue() { return totalDue; }
    public void setTotalDue(double totalDue) { this.totalDue = totalDue; }
}
