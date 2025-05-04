package com.electricity.system.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "billing_charges")
public class BillingCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chargeName;

    @Enumerated(EnumType.STRING)
    private ChargeType chargeType; // FIXED or PERCENTAGE

    private double value;
    private boolean active;
    private LocalDate effectiveFrom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public enum ChargeType {
        FIXED,
        PERCENTAGE
    }

    
}
