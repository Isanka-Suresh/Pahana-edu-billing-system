package com.pahanaEdu.model;

/**
 * Item entity representing billable items/services
 */
public class Item {
    private int itemId;
    private String itemCode;
    private String itemName;
    private String description;
    private double unitPrice;
    private String category;
    private int stockQuantity;
    
    // Default constructor
    public Item() {
    }
    
    // Constructor with all fields
    public Item(int itemId, String itemCode, String itemName, String description, double unitPrice, String category, int stockQuantity) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }
    
    // Constructor without ID (for new items)
    public Item(String itemCode, String itemName, String description, double unitPrice, String category, int stockQuantity) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }
    
    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", category='" + category + '\'' +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
