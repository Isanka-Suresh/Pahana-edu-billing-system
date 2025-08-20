package com.pahanaEdu.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.pahanaEdu.model.Order;
import com.pahanaEdu.model.OrderItem;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating PDF bills
 */
public class BillPDFGenerator {

    /**
     * Generate a PDF bill for an order
     * 
     * @param order The order to generate a bill for
     * @param outputStream The output stream to write the PDF to
     * @throws Exception If an error occurs during PDF generation
     */
    public static void generateBill(Order order, OutputStream outputStream) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        // Add header
        addHeader(document, order);
        
        // Add customer and order information
        addCustomerAndOrderInfo(document, order);
        
        // Add items table
        addItemsTable(document, order);
        
        // Add totals
        addTotals(document, order);
        
        // Add footer
        addFooter(document);
        
        document.close();
    }
    
    private static void addHeader(Document document, Order order) throws DocumentException {
        // Add logo (if available)
        try {
            Image logo = Image.getInstance(BillPDFGenerator.class.getResource("/logo.png"));
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // Logo not available, use text instead
            Paragraph title = new Paragraph("Pahana Educational Institute", 
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
        }
        
        // Add company info
        Paragraph companyInfo = new Paragraph(
                "123 Education Street, Colombo, Sri Lanka\n" +
                "Tel: +94 11 2345678 | Email: info@pahanaedu.lk\n" +
                "www.pahanaedu.lk",
                new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY));
        companyInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(companyInfo);
        
        // Add invoice title
        Paragraph invoiceTitle = new Paragraph("INVOICE", 
                new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));
        invoiceTitle.setAlignment(Element.ALIGN_CENTER);
        invoiceTitle.setSpacingBefore(20);
        invoiceTitle.setSpacingAfter(20);
        document.add(invoiceTitle);
    }
    
    private static void addCustomerAndOrderInfo(Document document, Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        
        // Customer info
        PdfPCell customerCell = new PdfPCell();
        customerCell.setBorder(Rectangle.NO_BORDER);
        customerCell.addElement(new Paragraph("Bill To:", 
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        customerCell.addElement(new Paragraph(order.getCustomer().getFullName(),
                new Font(Font.FontFamily.HELVETICA, 11)));
        customerCell.addElement(new Paragraph("Account #: " + order.getCustomer().getAccountNumber(),
                new Font(Font.FontFamily.HELVETICA, 11)));
        customerCell.addElement(new Paragraph(order.getCustomer().getAddress(),
                new Font(Font.FontFamily.HELVETICA, 11)));
        customerCell.addElement(new Paragraph("Tel: " + order.getCustomer().getPhone(),
                new Font(Font.FontFamily.HELVETICA, 11)));
        table.addCell(customerCell);
        
        // Invoice info
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        Paragraph invoiceInfo = new Paragraph();
        invoiceInfo.setAlignment(Element.ALIGN_RIGHT);
        invoiceInfo.add(new Chunk("Invoice #: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        invoiceInfo.add(new Chunk("INV-" + order.getOrderId() + "\n", new Font(Font.FontFamily.HELVETICA, 11)));
        
        invoiceInfo.add(new Chunk("Date: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        invoiceInfo.add(new Chunk(dateFormat.format(order.getOrderDate()) + "\n", new Font(Font.FontFamily.HELVETICA, 11)));
        
        invoiceInfo.add(new Chunk("Status: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        String paymentStatus = order.getPaymentStatus();
        paymentStatus = paymentStatus.substring(0, 1).toUpperCase() + paymentStatus.substring(1);
        invoiceInfo.add(new Chunk(paymentStatus, new Font(Font.FontFamily.HELVETICA, 11)));
        
        invoiceCell.addElement(invoiceInfo);
        table.addCell(invoiceCell);
        
        document.add(table);
        
        // Add spacing
        document.add(new Paragraph(" "));
    }
    
    private static void addItemsTable(Document document, Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        
        // Set column widths
        float[] columnWidths = {5f, 40f, 15f, 15f, 25f};
        table.setWidths(columnWidths);
        
        // Add table header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerBgColor = new BaseColor(66, 66, 66);
        
        PdfPCell headerCell;
        
        headerCell = new PdfPCell(new Phrase("#", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("Item Description", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("Price", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("Quantity", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("Amount", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        
        // Add table rows
        Font rowFont = new Font(Font.FontFamily.HELVETICA, 11);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        
        int index = 1;
        for (OrderItem item : order.getOrderItems()) {
            // Item number
            PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(index++), rowFont));
            cell.setPadding(5);
            table.addCell(cell);
            
            // Item name
            cell = new PdfPCell(new Phrase(item.getItem().getItemName(), rowFont));
            cell.setPadding(5);
            table.addCell(cell);
            
            // Price
            cell = new PdfPCell(new Phrase("Rs. " + decimalFormat.format(item.getItem().getUnitPrice()), rowFont));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            // Quantity
            cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            // Subtotal
            cell = new PdfPCell(new Phrase("Rs. " + decimalFormat.format(item.getLineTotal()), rowFont));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        
        document.add(table);
    }
    
    private static void addTotals(Document document, Order order) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{75f, 25f});
        table.setSpacingBefore(10);
        
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 11);
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        
        // Subtotal
        PdfPCell labelCell = new PdfPCell(new Phrase("Subtotal:", labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPadding(5);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase("Rs. " + decimalFormat.format(order.getTotalAmount()), valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPadding(5);
        table.addCell(valueCell);
        
        // Tax
        labelCell = new PdfPCell(new Phrase("Tax (0%):", labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPadding(5);
        table.addCell(labelCell);
        
        valueCell = new PdfPCell(new Phrase("Rs. 0.00", valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPadding(5);
        table.addCell(valueCell);
        
        // Total
        labelCell = new PdfPCell(new Phrase("Total:", totalFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPadding(5);
        table.addCell(labelCell);
        
        valueCell = new PdfPCell(new Phrase("Rs. " + decimalFormat.format(order.getTotalAmount()), totalFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPadding(5);
        table.addCell(valueCell);
        
        document.add(table);
        
        // Payment terms
        Paragraph paymentTerms = new Paragraph();
        paymentTerms.setSpacingBefore(20);
        paymentTerms.add(new Chunk("Payment Terms: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        paymentTerms.add(new Chunk("Due on receipt", new Font(Font.FontFamily.HELVETICA, 11)));
        document.add(paymentTerms);
        
        // Notes
//        Paragraph notes = new Paragraph();
//        notes.setSpacingBefore(10);
//        notes.add(new Chunk("Notes: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
//        notes.add(new Chunk(order.get() != null && !order.getNotes().isEmpty() ?
//                order.getNotes() : "Thank you for your business!", new Font(Font.FontFamily.HELVETICA, 11)));
//        document.add(notes);
    }
    
    private static void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph();
        footer.setSpacingBefore(40);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.add(new Chunk("This is a computer-generated invoice and does not require a signature.\n", 
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY)));
        footer.add(new Chunk("Pahana Educational Institute | www.pahanaedu.lk", 
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY)));
        document.add(footer);
    }
}
