package com.electricity.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.electricity.system.model.MeterReading;

import jakarta.servlet.http.HttpServletResponse;

public class CustomerReadingHistoryController {
    @GetMapping("/user/download-bill/{id}")
public void downloadBillPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
    MeterReading reading = meterReadingService.getById(id);

    // Set PDF response headers
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

    // Use iText to generate PDF
    Document document = new Document();
    PdfWriter.getInstance(document, response.getOutputStream());
    document.open();

    document.add(new Paragraph("Electricity Bill"));
    document.add(new Paragraph("Customer: " + reading.getCustomer().getFullName()));
    document.add(new Paragraph("Meter No: " + reading.getCustomer().getMeterNumber()));
    document.add(new Paragraph("Billing Month: " + reading.getBillingMonth() + "/" + reading.getBillingYear()));
    document.add(new Paragraph("Units Consumed: " + reading.getUnitsConsumed()));
    document.add(new Paragraph("Rate per Unit: " + reading.getRatePerUnit()));
    document.add(new Paragraph("Total Amount: ₹" + reading.getTotalAmount()));
    document.add(new Paragraph("Amount Paid: ₹" + reading.getAmountPaid()));
    document.add(new Paragraph("Total Due: ₹" + reading.getTotalDue()));
    document.add(new Paragraph("Status: " + reading.getStatus()));

    document.close();
}

}
