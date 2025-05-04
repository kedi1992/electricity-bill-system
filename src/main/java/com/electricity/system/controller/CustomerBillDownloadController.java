// 1. CONTROLLER: CustomerBillDownloadController.java

package com.electricity.system.controller;

import com.electricity.system.model.MeterReading;
import com.electricity.system.service.MeterReadingService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class CustomerBillDownloadController {

    @Autowired
    private MeterReadingService meterReadingService;

    @GetMapping("/user/download-bill/{id}")
    public void downloadBillPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        MeterReading reading = meterReadingService.getById(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA);

        // Header with Company Name and Logo Placeholder
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{1, 3});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        try {
            String imagePath = new ClassPathResource("static/img/logo.png").getFile().getAbsolutePath();
            Image logo = Image.getInstance(imagePath);
            logo.scaleToFit(50, 50);
            logoCell.addElement(logo);
        } catch (Exception e) {
            logoCell.addElement(new Paragraph("ePowerGrid", titleFont));
        }

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.addElement(new Paragraph("ENERGY STATEMENT", titleFont));
        infoCell.addElement(new Paragraph("Account No: " + reading.getCustomer().getMeterNumber(), normalFont));
        infoCell.addElement(new Paragraph("Statement Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), normalFont));
        infoCell.addElement(new Paragraph("Due Date: " + LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), normalFont));

        headerTable.addCell(logoCell);
        headerTable.addCell(infoCell);
        document.add(headerTable);

        document.add(Chunk.NEWLINE);

        // Account Summary
        Paragraph summaryTitle = new Paragraph("Your Account Summary", labelFont);
        summaryTitle.setSpacingBefore(10);
        document.add(summaryTitle);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        summaryTable.setSpacingBefore(5);
        summaryTable.addCell(new PdfPCell(new Phrase("Previous Balance:", labelFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("₹" + reading.getTotalAmount(), normalFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("Payment Received:", labelFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("₹" + reading.getAmountPaid(), normalFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("Current Delivery Charges:", labelFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("₹55.66", normalFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("Generation Charges:", labelFont)));
        summaryTable.addCell(new PdfPCell(new Phrase("₹32.48", normalFont)));

        document.add(summaryTable);

        document.add(Chunk.NEWLINE);

        // Highlighted Total Due Section
        PdfPTable totalDueTable = new PdfPTable(1);
        totalDueTable.setWidthPercentage(100);
        PdfPCell totalDueCell = new PdfPCell();
        totalDueCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalDueCell.setPadding(10);

        Paragraph totalDueTitle = new Paragraph("Total Amount Due by " + LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), labelFont);
        totalDueTitle.setAlignment(Element.ALIGN_CENTER);
        Paragraph amount = new Paragraph("₹" + reading.getTotalDue(), titleFont);
        amount.setAlignment(Element.ALIGN_CENTER);

        totalDueCell.addElement(totalDueTitle);
        totalDueCell.addElement(amount);
        totalDueTable.addCell(totalDueCell);

        document.add(totalDueTable);

        document.close();
    }
}
