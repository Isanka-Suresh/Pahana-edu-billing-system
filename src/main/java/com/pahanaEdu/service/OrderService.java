package com.pahanaEdu.service;

import com.pahanaEdu.dao.ItemDAO;
import com.pahanaEdu.dao.OrderDAO;
import com.pahanaEdu.dao.OrderItemDAO;
import com.pahanaEdu.model.Item;
import com.pahanaEdu.model.Order;
import com.pahanaEdu.model.OrderItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for order management
 */
public class OrderService {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ItemDAO itemDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.itemDAO = new ItemDAO();
    }
    
    /**
     * Create a new order
     * @param order Order object to create
     * @return true if successful, false otherwise
     */
    public boolean createOrder(Order order) {
        if (!validateOrder(order)) {
            return false;
        }
        
        // Generate order number if not provided
        if (order.getOrderNumber() == null || order.getOrderNumber().trim().isEmpty()) {
            order.setOrderNumber(orderDAO.generateOrderNumber());
        }
        
        // Set order date if not provided
        if (order.getOrderDate() == null) {
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        }
        
        // Set default status if not provided
        if (order.getOrderStatus() == null || order.getOrderStatus().trim().isEmpty()) {
            order.setOrderStatus("Pending");
        }
        
        // Set default payment status if not provided
        if (order.getPaymentStatus() == null || order.getPaymentStatus().trim().isEmpty()) {
            order.setPaymentStatus("Unpaid");
        }
        
        // Calculate total amount
        order.calculateTotal();
        
        // Update stock quantities
        if (!updateStockQuantities(order, true)) {
            return false;
        }
        
        return orderDAO.insertOrder(order);
    }
    
    /**
     * Update an existing order
     * @param order Order object to update
     * @return true if successful, false otherwise
     */
    public boolean updateOrder(Order order) {
        if (!validateOrder(order)) {
            return false;
        }
        
        // Get the original order to restore stock quantities
        Order originalOrder = orderDAO.getOrderById(order.getOrderId());
        if (originalOrder != null) {
            // Restore original stock quantities
            updateStockQuantities(originalOrder, false);
        }
        
        // Calculate total amount
        order.calculateTotal();
        
        // Update stock quantities for the updated order
        if (!updateStockQuantities(order, true)) {
            // If stock update fails, restore original stock quantities
            if (originalOrder != null) {
                updateStockQuantities(originalOrder, true);
            }
            return false;
        }
        
        return orderDAO.updateOrder(order);
    }
    
    /**
     * Delete an order
     * @param orderId ID of the order to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteOrder(int orderId) {
        if (orderId <= 0) {
            return false;
        }
        
        // Get the order to restore stock quantities
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            // Restore stock quantities
            updateStockQuantities(order, false);
        }
        
        return orderDAO.deleteOrder(orderId);
    }
    
    /**
     * Get an order by ID
     * @param orderId ID to search for
     * @return Order object if found, null otherwise
     */
    public Order getOrderById(int orderId) {
        if (orderId <= 0) {
            return null;
        }
        
        return orderDAO.getOrderById(orderId);
    }

    /**
     * Get orders by status
     * @param orderStatus Status to filter by (e.g., "pending", "completed", "cancelled")
     * @return List of orders with the specified status
     */
    public List<Order> getOrdersByStatus(String orderStatus) {
        if (orderStatus == null || orderStatus.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return orderDAO.getOrdersByStatus(orderStatus);
    }
    
    /**
     * Get all orders
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    /**
     * Get orders for a specific customer
     * @param customerId ID of the customer
     * @return List of orders for the customer
     */
    public List<Order> getOrdersByCustomerId(int customerId) {
        if (customerId <= 0) {
            return null;
        }
        
        return orderDAO.getOrdersByCustomerId(customerId);
    }
    
    /**
     * Update order status
     * @param orderId ID of the order
     * @param status New status value
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, String status) {
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }
        
        return orderDAO.updateOrderStatus(orderId, status);
    }
    
    /**
     * Update payment status
     * @param orderId ID of the order
     * @param status New payment status value
     * @return true if successful, false otherwise
     */
    public boolean updatePaymentStatus(int orderId, String status) {
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }
        
        return orderDAO.updatePaymentStatus(orderId, status);
    }
    
    /**
     * Add an item to an order
     * @param orderId ID of the order
     * @param itemId ID of the item to add
     * @param quantity Quantity to add
     * @return true if successful, false otherwise
     */
    public boolean addItemToOrder(int orderId, int itemId, double quantity) {
        if (orderId <= 0 || itemId <= 0 || quantity <= 0) {
            return false;
        }
        
        // Get the order
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            return false;
        }
        
        // Get the item
        Item item = itemDAO.getItemById(itemId);
        if (item == null) {
            return false;
        }
        
        // Check if there's enough stock
        if (item.getStockQuantity() < quantity) {
            return false;
        }
        
        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setItemId(itemId);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(item.getUnitPrice());
        orderItem.calculateLineTotal();
        orderItem.setItem(item);
        
        // Add to order
        order.addOrderItem(orderItem);
        
        // Update stock quantity
        itemDAO.updateStockQuantity(itemId, (int)(item.getStockQuantity() - quantity));
        
        // Update order total
        order.calculateTotal();
        
        // Update order in database
        return orderDAO.updateOrder(order);
    }
    
    /**
     * Remove an item from an order
     * @param orderItemId ID of the order item to remove
     * @return true if successful, false otherwise
     */
    public boolean removeItemFromOrder(int orderItemId) {
        if (orderItemId <= 0) {
            return false;
        }
        
        // Get the order item
        OrderItem orderItem = orderItemDAO.getOrderItemById(orderItemId);
        if (orderItem == null) {
            return false;
        }
        
        // Get the order
        Order order = orderDAO.getOrderById(orderItem.getOrderId());
        if (order == null) {
            return false;
        }
        
        // Get the item
        Item item = itemDAO.getItemById(orderItem.getItemId());
        if (item == null) {
            return false;
        }
        
        // Restore stock quantity
        itemDAO.updateStockQuantity(orderItem.getItemId(), (int)(item.getStockQuantity() + orderItem.getQuantity()));
        
        // Remove order item
        if (!orderItemDAO.deleteOrderItem(orderItemId)) {
            return false;
        }
        
        // Update order total
        order.getOrderItems().removeIf(item1 -> item1.getOrderItemId() == orderItemId);
        order.calculateTotal();
        
        // Update order in database
        return orderDAO.updateOrder(order);
    }
    
    /**
     * Update stock quantities based on order items
     * @param order Order containing items
     * @param decrease true to decrease stock, false to increase (restore) stock
     * @return true if successful, false otherwise
     */
    private boolean updateStockQuantities(Order order, boolean decrease) {
        if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return true;
        }
        
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = itemDAO.getItemById(orderItem.getItemId());
            if (item == null) {
                continue;
            }
            
            int newQuantity;
            if (decrease) {
                // Check if there's enough stock
                if (item.getStockQuantity() < orderItem.getQuantity()) {
                    return false;
                }
                newQuantity = (int)(item.getStockQuantity() - orderItem.getQuantity());
            } else {
                newQuantity = (int)(item.getStockQuantity() + orderItem.getQuantity());
            }
            
            if (!itemDAO.updateStockQuantity(item.getItemId(), newQuantity)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validate order data
     * @param order Order object to validate
     * @return true if valid, false otherwise
     */
    private boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }
        
        if (order.getCustomerId() <= 0) {
            return false;
        }
        
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return false;
        }
        
        return true;
    }

    public List<Order> searchOrders(String searchTerm, String string, String paymentStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchOrders'");
    }
}
