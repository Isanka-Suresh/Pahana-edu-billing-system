package com.pahanaEdu.model;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int itemId;
    private Item item;
    private double quantity;
    private double unitPrice;
    private double lineTotal;
    
    // Default constructor
    public OrderItem() {
    }
    
    // Constructor with all fields
    public OrderItem(int orderItemId, int orderId, int itemId, Item item, double quantity, double unitPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.calculateLineTotal();
    }
    
    // Constructor without ID (for new order items)
    public OrderItem(int orderId, int itemId, Item item, double quantity, double unitPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.calculateLineTotal();
    }
    
    // Method to calculate line total
    public void calculateLineTotal() {
        this.lineTotal = this.quantity * this.unitPrice;
    }
    
    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        calculateLineTotal();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateLineTotal();
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", lineTotal=" + lineTotal +
                '}';
    }
}
