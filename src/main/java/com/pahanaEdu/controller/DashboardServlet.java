package com.pahanaEdu.controller;

import com.pahanaEdu.service.CustomerService;
import com.pahanaEdu.service.ItemService;
import com.pahanaEdu.service.OrderService;
import com.pahanaEdu.service.BillService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling dashboard requests
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private CustomerService customerService;
    private ItemService itemService;
    private OrderService orderService;
    private BillService billService;
    
    @Override
    public void init() {
        customerService = new CustomerService();
        itemService = new ItemService();
        orderService = new OrderService();
        billService = new BillService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get counts for dashboard
        int customerCount = customerService.getAllCustomers().size();
        int itemCount = itemService.getAllItems().size();
        int orderCount = orderService.getAllOrders().size();
        int billCount = billService.getAllBills().size();
        
        // Set attributes for dashboard
        request.setAttribute("customerCount", customerCount);
        request.setAttribute("itemCount", itemCount);
        request.setAttribute("orderCount", orderCount);
        request.setAttribute("billCount", billCount);
        
        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
