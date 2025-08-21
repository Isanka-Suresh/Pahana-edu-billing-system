package com.pahanaEdu.controller;

import com.pahanaEdu.model.Customer;
import com.pahanaEdu.model.Item;
import com.pahanaEdu.model.Order;
import com.pahanaEdu.model.OrderItem;
import com.pahanaEdu.service.CustomerService;
import com.pahanaEdu.service.ItemService;
import com.pahanaEdu.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet for handling order management
 */
@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    private OrderService orderService;
    private CustomerService customerService;
    private ItemService itemService;
    
    @Override
    public void init() {
        orderService = new OrderService();
        customerService = new CustomerService();
        itemService = new ItemService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all orders
            listOrders(request, response);
        } else if (pathInfo.equals("/create")) {
            // Show create order form
            showCreateForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Show edit order form
            showEditForm(request, response);
        } else if (pathInfo.equals("/view")) {
            // View order details
            viewOrderDetails(request, response);
        } else if (pathInfo.equals("/delete")) {
            // Delete order
            deleteOrder(request, response);
        } else if (pathInfo.equals("/cancel")) {
            // Cancel order
            cancelOrder(request, response);
        } else if (pathInfo.equals("/complete")) {
            // Complete order
            completeOrder(request, response);
        } else {
            // Invalid path, redirect to order list
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Create new order
            createOrder(request, response);
        } else if (pathInfo.equals("/update")) {
            // Update existing order
            updateOrder(request, response);
        } else if (pathInfo.equals("/add-item")) {
            // Add item to order
            addItemToOrder(request, response);
        } else if (pathInfo.equals("/remove-item")) {
            // Remove item from order
            removeItemFromOrder(request, response);
        } else if (pathInfo.equals("/search")) {
            // Search orders
            searchOrders(request, response);
        } else {
            // Invalid path, redirect to order list
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }
    
    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> orders = orderService.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all customers and items for the form
        List<Customer> customers = customerService.getAllCustomers();
        List<Item> items = itemService.getAllItems();
        
        request.setAttribute("customers", customers);
        request.setAttribute("items", items);
        
        // Check if there's an order in session (for adding items)
        HttpSession session = request.getSession();
        Order sessionOrder = (Order) session.getAttribute("currentOrder");
        
        if (sessionOrder == null) {
            // Create a new order in session
            sessionOrder = new Order();
            sessionOrder.setOrderItems(new ArrayList<>());
            sessionOrder.setOrderDate(new Timestamp(new Date().getTime()));
            sessionOrder.setOrderStatus("pending");
            sessionOrder.setPaymentStatus("pending");
            session.setAttribute("currentOrder", sessionOrder);
        }
        
        request.setAttribute("order", sessionOrder);
        request.getRequestDispatcher("/WEB-INF/views/order/create.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderService.getOrderById(orderId);
                
                if (order != null) {
                    // Get all customers and items for the form
                    List<Customer> customers = customerService.getAllCustomers();
                    List<Item> items = itemService.getAllItems();
                    
                    request.setAttribute("customers", customers);
                    request.setAttribute("items", items);
                    request.setAttribute("order", order);
                    
                    // Store order in session for editing
                    HttpSession session = request.getSession();
                    session.setAttribute("currentOrder", order);
                    
                    request.getRequestDispatcher("/WEB-INF/views/order/edit.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Order not found or invalid order ID
        response.sendRedirect(request.getContextPath() + "/orders");
    }
    
    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderService.getOrderById(orderId);
                
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/order/view.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Order not found or invalid order ID
        response.sendRedirect(request.getContextPath() + "/orders");
    }
    
    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order sessionOrder = (Order) session.getAttribute("currentOrder");
        
        if (sessionOrder == null || sessionOrder.getOrderItems().isEmpty()) {
            // No order in session or no items added
            request.setAttribute("errorMessage", "Cannot create order without items");
            showCreateForm(request, response);
            return;
        }
        
        String customerIdStr = request.getParameter("customerId");
        String notes = request.getParameter("notes");
        
        // Validate customer
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please select a customer");
            showCreateForm(request, response);
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            Customer customer = customerService.getCustomer(customerId);
            
            if (customer == null) {
                request.setAttribute("errorMessage", "Invalid customer selected");
                showCreateForm(request, response);
                return;
            }
            
            // Update order with form data
            sessionOrder.setCustomerId(customerId);
            sessionOrder.setCustomer(customer);
            // Notes field not available in Order model
            // sessionOrder.setNotes(notes != null ? notes : "");
            
            // Create order in database
            if (orderService.createOrder(sessionOrder)) {
                // Success, clear session and redirect to order list
                session.removeAttribute("currentOrder");
                response.sendRedirect(request.getContextPath() + "/orders");
            } else {
                // Failed to create order
                request.setAttribute("errorMessage", "Failed to create order. Please check stock availability.");
                showCreateForm(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid customer ID");
            showCreateForm(request, response);
        }
    }
    
    private void updateOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order sessionOrder = (Order) session.getAttribute("currentOrder");
        
        if (sessionOrder == null) {
            // No order in session
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        
        String orderIdStr = request.getParameter("orderId");
        String customerIdStr = request.getParameter("customerId");
        String notes = request.getParameter("notes");
        String status = request.getParameter("status");
        String paymentStatus = request.getParameter("paymentStatus");
        
        // Validate input
        if (orderIdStr == null || orderIdStr.trim().isEmpty() ||
            customerIdStr == null || customerIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            int customerId = Integer.parseInt(customerIdStr);
            
            // Update order with form data
            sessionOrder.setOrderId(orderId);
            sessionOrder.setCustomerId(customerId);
            // Notes field not available in Order model
            // sessionOrder.setNotes(notes != null ? notes : "");
            sessionOrder.setOrderStatus(status != null ? status : "pending");
            sessionOrder.setPaymentStatus(paymentStatus != null ? paymentStatus : "pending");
            
            // Update order in database
            if (orderService.updateOrder(sessionOrder)) {
                // Success, clear session and redirect to order list
                session.removeAttribute("currentOrder");
                response.sendRedirect(request.getContextPath() + "/orders");
            } else {
                // Failed to update order
                request.setAttribute("errorMessage", "Failed to update order. Please check stock availability.");
                showEditForm(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }
    
    private void addItemToOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order sessionOrder = (Order) session.getAttribute("currentOrder");
        
        if (sessionOrder == null) {
            // No order in session, create a new one
            sessionOrder = new Order();
            sessionOrder.setOrderItems(new ArrayList<>());
            sessionOrder.setOrderDate(new Timestamp(new Date().getTime()));
            sessionOrder.setOrderStatus("pending");
            sessionOrder.setPaymentStatus("pending");
        }
        
        // Preserve customer selection if provided
        String customerIdStr = request.getParameter("customerId");
        if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
            try {
                int customerId = Integer.parseInt(customerIdStr);
                Customer customer = customerService.getCustomer(customerId);
                if (customer != null) {
                    sessionOrder.setCustomerId(customerId);
                    sessionOrder.setCustomer(customer);
                }
            } catch (NumberFormatException e) {
                // Invalid customer ID, ignore
            }
        }
        
        String itemIdStr = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity");
        
        // Validate input
        if (itemIdStr == null || itemIdStr.trim().isEmpty() ||
            quantityStr == null || quantityStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Item and quantity are required");
            
            // Determine which form to show
            if (sessionOrder.getOrderId() > 0) {
                showEditForm(request, response);
            } else {
                showCreateForm(request, response);
            }
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                request.setAttribute("errorMessage", "Quantity must be greater than zero");
                
                // Determine which form to show
                if (sessionOrder.getOrderId() > 0) {
                    showEditForm(request, response);
                } else {
                    showCreateForm(request, response);
                }
                return;
            }
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                request.setAttribute("errorMessage", "Invalid item selected");
                
                // Determine which form to show
                if (sessionOrder.getOrderId() > 0) {
                    showEditForm(request, response);
                } else {
                    showCreateForm(request, response);
                }
                return;
            }
            
            // Check if item is already in order
            boolean itemExists = false;
            for (OrderItem orderItem : sessionOrder.getOrderItems()) {
                if (orderItem.getItem() != null && orderItem.getItem().getItemId() == itemId) {
                    // Update quantity
                    orderItem.setQuantity(orderItem.getQuantity() + quantity);
                    orderItem.setLineTotal(orderItem.getQuantity() * item.getUnitPrice());
                    itemExists = true;
                    break;
                }
            }
            
            if (!itemExists) {
                // Add new item to order
                OrderItem orderItem = new OrderItem();
                orderItem.setItemId(itemId);
                orderItem.setItem(item);
                orderItem.setUnitPrice(item.getUnitPrice());
                orderItem.setQuantity(quantity);
                orderItem.setLineTotal(quantity * item.getUnitPrice());
                sessionOrder.getOrderItems().add(orderItem);
            }
            
            // Update order total
            double total = 0;
            for (OrderItem orderItem : sessionOrder.getOrderItems()) {
                total += orderItem.getLineTotal();
            }
            sessionOrder.setTotalAmount(total);
            
            // Update session
            session.setAttribute("currentOrder", sessionOrder);
            
            // Determine which form to show
            if (sessionOrder.getOrderId() > 0) {
                showEditForm(request, response);
            } else {
                showCreateForm(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid item ID or quantity");
            
            // Determine which form to show
            if (sessionOrder.getOrderId() > 0) {
                showEditForm(request, response);
            } else {
                showCreateForm(request, response);
            }
        }
    }
    
    private void removeItemFromOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order sessionOrder = (Order) session.getAttribute("currentOrder");
        
        if (sessionOrder == null) {
            // No order in session
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        
        String itemIdStr = request.getParameter("itemId");
        
        if (itemIdStr != null && !itemIdStr.trim().isEmpty()) {
            try {
                int itemId = Integer.parseInt(itemIdStr);
                
                // Remove item from order
                List<OrderItem> updatedItems = new ArrayList<>();
                for (OrderItem orderItem : sessionOrder.getOrderItems()) {
                    if (orderItem.getItem() == null || orderItem.getItem().getItemId() != itemId) {
                        updatedItems.add(orderItem);
                    }
                }
                sessionOrder.setOrderItems(updatedItems);
                
                // Update order total
                double total = 0;
                for (OrderItem orderItem : sessionOrder.getOrderItems()) {
                    total += orderItem.getLineTotal();
                }
                sessionOrder.setTotalAmount(total);
                
                // Update session
                session.setAttribute("currentOrder", sessionOrder);
            } catch (NumberFormatException e) {
                // Invalid item ID format
            }
        }
        
        // Determine which form to show
        if (sessionOrder.getOrderId() > 0) {
            showEditForm(request, response);
        } else {
            showCreateForm(request, response);
        }
    }
    
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                orderService.deleteOrder(orderId);
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Redirect to order list
        response.sendRedirect(request.getContextPath() + "/orders");
    }
    
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                orderService.updateOrderStatus(orderId, "cancelled");
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Redirect to order list
        response.sendRedirect(request.getContextPath() + "/orders");
    }
    
    private void completeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                orderService.updateOrderStatus(orderId, "completed");
            } catch (NumberFormatException e) {
                // Invalid order ID format
            }
        }
        
        // Redirect to order list
        response.sendRedirect(request.getContextPath() + "/orders");
    }
    
    private void searchOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String status = request.getParameter("status");
        String paymentStatus = request.getParameter("paymentStatus");
        
        List<Order> orders;
        
        if ((searchTerm == null || searchTerm.trim().isEmpty()) &&
            (status == null || status.trim().isEmpty()) &&
            (paymentStatus == null || paymentStatus.trim().isEmpty())) {
            // No search criteria, get all orders
            orders = orderService.getAllOrders();
        } else {
            // Search by criteria using the searchOrders method
            orders = orderService.searchOrders(searchTerm, status, paymentStatus);
            // No need for additional filtering as it's handled in the service layer
        }
        
        request.setAttribute("orders", orders);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("selectedPaymentStatus", paymentStatus);
        request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
    }
}
