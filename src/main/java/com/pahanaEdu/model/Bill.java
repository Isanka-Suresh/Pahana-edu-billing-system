package com.pahanaEdu.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Bill entity representing customer bills in the system
 */
public class Bill {
    private int billId;
    private String accountNo;
    private int customerId;
    private String billNumber;
    private Timestamp billDate;
    private int units;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus; // pending, paid, cancelled
    private Customer customer;
    
    // Default constructor
    public Bill() {
        this.billDate = new Timestamp(System.currentTimeMillis());
        this.paymentStatus = "pending";
    }
    
    // Constructor with all fields
    public Bill(int billId, String accountNo, int customerId, String billNumber, Timestamp billDate, 
               int units, BigDecimal totalAmount, String paymentMethod, String paymentStatus) {
        this.billId = billId;
        this.accountNo = accountNo;
        this.customerId = customerId;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.units = units;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
    
    // Constructor without ID (for new bills)
    public Bill(String accountNo, int customerId, String billNumber, Timestamp billDate, 
               int units, BigDecimal totalAmount, String paymentMethod) {
        this.accountNo = accountNo;
        this.customerId = customerId;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.units = units;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "pending";
    }
    
    // Method to calculate bill amount based on units consumed
    public void calculateBill() {
        // Simple calculation based on units consumed
        // Can be enhanced with different rate slabs
        double rate = 10.0; // Base rate per unit
        double amount = this.units * rate;
        
        // Add fixed charges
        double fixedCharges = 100.0;
        amount += fixedCharges;
        
        // Add tax (e.g., 5%)
        double tax = amount * 0.05;
        amount += tax;
        
        this.totalAmount = BigDecimal.valueOf(amount);
    }
    
    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }
    
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }
    
    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", accountNo='" + accountNo + '\'' +
                ", customerId=" + customerId +
                ", billNumber='" + billNumber + '\'' +
                ", billDate=" + billDate +
                ", units=" + units +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
