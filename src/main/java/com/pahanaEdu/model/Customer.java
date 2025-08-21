package com.pahanaEdu.model;

public class Customer {
    private int customerId;
    private String accountNumber;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    
    // Default constructor
    public Customer() {
    }
    
    // Constructor with fields
    public Customer(int customerId, String accountNumber, String fullName, String address, String phone, String email, int unitsConsumed) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
    
    // Constructor without ID (for new customers)
    public Customer(String accountNumber, String fullName, String address, String phone, String email, int unitsConsumed) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
    
    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", accountNumber='" + accountNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
