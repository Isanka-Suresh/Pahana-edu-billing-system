package com.pahanaEdu.controller;

import com.pahanaEdu.model.Customer;
import com.pahanaEdu.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService;
    
    @Override
    public void init() {
        customerService = new CustomerService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all customers
            listCustomers(request, response);
        } else if (pathInfo.equals("/add")) {
            // Show add customer form
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Show edit customer form
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            // Delete customer
            deleteCustomer(request, response);
        } else {
            // Invalid path, redirect to customer list
            response.sendRedirect(request.getContextPath() + "/customers");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Add new customer
            addCustomer(request, response);
        } else if (pathInfo.equals("/update")) {
            // Update existing customer
            updateCustomer(request, response);
        } else if (pathInfo.equals("/search")) {
            // Search customers
            searchCustomers(request, response);
        } else {
            // Invalid path, redirect to customer list
            response.sendRedirect(request.getContextPath() + "/customers");
        }
    }
    
    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customers = customerService.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            Customer customer = customerService.getCustomerByAccountNumber(accountNumber);
            
            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/WEB-INF/views/customer/edit.jsp").forward(request, response);
                return;
            }
        }
        
        // Customer not found or invalid account number
        response.sendRedirect(request.getContextPath() + "/customers");
    }
    
    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String unitsStr = request.getParameter("unitsConsumed");
        
        // Validate input
        if (accountNumber == null || accountNumber.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Account number and full name are required");
            request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp").forward(request, response);
            return;
        }
        
        // Parse units consumed
        int unitsConsumed = 0;
        try {
            if (unitsStr != null && !unitsStr.trim().isEmpty()) {
                unitsConsumed = Integer.parseInt(unitsStr);
                if (unitsConsumed < 0) {
                    request.setAttribute("errorMessage", "Units consumed must be a non-negative number");
                    request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Units consumed must be a valid number");
            request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp").forward(request, response);
            return;
        }
        
        // Create customer object
        Customer customer = new Customer();
        customer.setAccountNumber(accountNumber);
        customer.setFullName(fullName);
        customer.setAddress(address != null ? address : "");
        customer.setPhone(phone != null ? phone : "");
        
        // Add customer
        if (customerService.addCustomer(customer)) {
            // Success, redirect to customer list
            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            // Failed, show error message
            request.setAttribute("errorMessage", "Failed to add customer. Account number may already exist.");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp").forward(request, response);
        }
    }
    
    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String unitsStr = request.getParameter("unitsConsumed");
        String customerIdStr = request.getParameter("customerId");
        
        // Validate input
        if (accountNumber == null || accountNumber.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            customerIdStr == null || customerIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/customers");
            return;
        }
        
        // Parse customer ID and units consumed
        int customerId;
        int unitsConsumed = 0;
        try {
            customerId = Integer.parseInt(customerIdStr);
            
            if (unitsStr != null && !unitsStr.trim().isEmpty()) {
                unitsConsumed = Integer.parseInt(unitsStr);
                if (unitsConsumed < 0) {
                    request.setAttribute("errorMessage", "Units consumed must be a non-negative number");
                    Customer customer = customerService.getCustomerByAccountNumber(accountNumber);
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher("/WEB-INF/views/customer/edit.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/customers");
            return;
        }
        
        // Create customer object
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setAccountNumber(accountNumber);
        customer.setFullName(fullName);
        customer.setAddress(address != null ? address : "");
        customer.setPhone(phone != null ? phone : "");
        
        // Update customer
        if (customerService.updateCustomer(customer)) {
            // Success, redirect to customer list
            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            // Failed, show error message
            request.setAttribute("errorMessage", "Failed to update customer");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/views/customer/edit.jsp").forward(request, response);
        }
    }
    
    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            customerService.deleteCustomer(accountNumber);
        }
        
        // Redirect to customer list
        response.sendRedirect(request.getContextPath() + "/customers");
    }
    
    private void searchCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        
        List<Customer> customers;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            customers = customerService.searchCustomersByName(searchTerm);
        } else {
            customers = customerService.getAllCustomers();
        }
        
        request.setAttribute("customers", customers);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }
}
