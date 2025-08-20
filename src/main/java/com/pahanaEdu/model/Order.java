package com.pahanaEdu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private String orderNumber;
    private int customerId;
    private Customer customer;
    private Timestamp orderDate;
    private double totalAmount;
    private String orderStatus; // pending, completed, cancelled
    private String paymentStatus; // pending, paid, refunded
    private List<OrderItem> orderItems;
    
    // Default constructor
    public Order() {
        this.orderItems = new ArrayList<>();
        this.orderStatus = "pending";
        this.paymentStatus = "pending";
    }
    
    // Constructor with all fields
    public Order(int orderId, String orderNumber, int customerId, Customer customer, 
                Timestamp orderDate, double totalAmount, String orderStatus, 
                String paymentStatus, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customer = customer;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }
    
    // Constructor without ID (for new orders)
    public Order(String orderNumber, int customerId, Customer customer, 
                Timestamp orderDate, String orderStatus, String paymentStatus) {
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customer = customer;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderItems = new ArrayList<>();
        this.totalAmount = 0.0;
    }
    
    // Method to add an order item
    public void addOrderItem(OrderItem item) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(item);
        this.totalAmount += item.getLineTotal();
    }
    
    // Method to calculate total amount
    public void calculateTotal() {
        this.totalAmount = 0.0;
        if (this.orderItems != null) {
            for (OrderItem item : this.orderItems) {
                this.totalAmount += item.getLineTotal();
            }
        }
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        calculateTotal();
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
