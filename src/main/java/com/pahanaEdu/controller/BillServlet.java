package com.pahanaEdu.controller;

import com.pahanaEdu.model.Order;
import com.pahanaEdu.service.OrderService;
import com.pahanaEdu.util.BillPDFGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling bill management
 */
@WebServlet("/bills/*")
public class BillServlet extends HttpServlet {
    private OrderService orderService;
    
    @Override
    public void init() {
        orderService = new OrderService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all bills (completed orders)
            listBills(request, response);
        } else if (pathInfo.equals("/generate")) {
            // Generate bill PDF
            generateBill(request, response);
        } else if (pathInfo.equals("/view")) {
            // View bill details
            viewBill(request, response);
        } else {
            // Invalid path, redirect to bills list
            response.sendRedirect(request.getContextPath() + "/bills");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.equals("/search")) {
            // Search bills
            searchBills(request, response);
        } else if (pathInfo != null && pathInfo.equals("/updatePaymentStatus")) {
            // Update payment status
            updatePaymentStatus(request, response);
        } else {
            // Invalid path, redirect to bills list
            response.sendRedirect(request.getContextPath() + "/bills");
        }
    }
    
    private void listBills(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all completed orders (bills)
        List<Order> bills = orderService.getOrdersByStatus("completed");
        request.setAttribute("bills", bills);
        request.getRequestDispatcher("/WEB-INF/views/bill/list.jsp").forward(request, response);
    }
    
    private void generateBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderService.getOrderById(orderId);
                
                if (order != null) {
                    // Set response headers for PDF download
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename=bill_" + orderId + ".pdf");
                    
                    // Generate PDF and write to response output stream
                    BillPDFGenerator.generateBill(order, response.getOutputStream());
                    
                    // If order is pending, mark it as completed
                    if ("pending".equals(order.getOrderStatus())) {
                        orderService.updateOrderStatus(orderId, "completed");
                    }
                    
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid order ID format
            } catch (Exception e) {
                // Error generating PDF
                request.setAttribute("errorMessage", "Error generating PDF: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }
        }
        
        // Order not found or invalid order ID
        response.sendRedirect(request.getContextPath() + "/bills");
    }
    
    private void viewBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderService.getOrderById(orderId);
                
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/bill/view.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Order not found or invalid order ID
        response.sendRedirect(request.getContextPath() + "/bills");
    }
    
    private void searchBills(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String paymentStatus = request.getParameter("paymentStatus");
        
        List<Order> bills;
        
        if ((searchTerm == null || searchTerm.trim().isEmpty()) &&
            (paymentStatus == null || paymentStatus.trim().isEmpty())) {
            // No search criteria, get all completed orders
            bills = orderService.getOrdersByStatus("completed");
        } else {
            // Search by criteria
            bills = orderService.searchOrders(searchTerm, "completed", paymentStatus);
        }
        
        request.setAttribute("bills", bills);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("selectedPaymentStatus", paymentStatus);
        request.getRequestDispatcher("/WEB-INF/views/bill/list.jsp").forward(request, response);
    }

    private void updatePaymentStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                
                // Validate status value
                if (status.equals("pending") || status.equals("paid") || status.equals("refunded")) {
                    // Update payment status
                    boolean success = orderService.updatePaymentStatus(orderId, status);
                    
                    if (success) {
                        // Redirect back to bill view
                        response.sendRedirect(request.getContextPath() + "/bills/view?orderId=" + orderId + "&statusUpdated=true");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // If we get here, there was an error
        response.sendRedirect(request.getContextPath() + "/bills?error=true");
    }
}
