package com.pahanaEdu.service;

import com.pahanaEdu.dao.BillDAO;
import com.pahanaEdu.dao.CustomerDAO;
import com.pahanaEdu.model.Bill;
import com.pahanaEdu.model.Customer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class BillService {
    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    
    public BillService() {
        this.billDAO = new BillDAO();
        this.customerDAO = new CustomerDAO();
    }

    public Bill generateBill(String accountNo) {
        Customer customer = customerDAO.getCustomerByAccountNumber(accountNo);
        if (customer == null) {
            return null;
        }
        
        Bill bill = new Bill();
        bill.setAccountNo(accountNo);
        bill.setCustomerId(customer.getCustomerId());
        bill.setBillNumber(generateBillNumber());
        bill.setBillDate(new Timestamp(System.currentTimeMillis()));
        bill.setCustomer(customer);
        
        // Calculate bill amount
        bill.calculateBill();
        
        // Save bill to database
        if (billDAO.insertBill(bill)) {
            return bill;
        }
        
        return null;
    }

    public boolean addBill(Bill bill) {
        if (!validateBill(bill)) {
            return false;
        }
        
        return billDAO.insertBill(bill);
    }

    public boolean updateBill(Bill bill) {
        if (!validateBill(bill)) {
            return false;
        }
        
        return billDAO.updateBill(bill);
    }

    public boolean deleteBill(int billId) {
        if (billId <= 0) {
            return false;
        }
        
        return billDAO.deleteBill(billId);
    }

    public Bill getBillById(int billId) {
        if (billId <= 0) {
            return null;
        }
        
        return billDAO.getBillById(billId);
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    public List<Bill> getBillsByAccountNo(String accountNo) {
        if (accountNo == null || accountNo.trim().isEmpty()) {
            return null;
        }
        
        return billDAO.getBillsByAccountNo(accountNo);
    }

    public List<Bill> getBillsByDateRange(Timestamp startDate, Timestamp endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return null;
        }
        
        return billDAO.getBillsByDateRange(startDate, endDate);
    }

    public BigDecimal getTotalRevenueByDateRange(Timestamp startDate, Timestamp endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return BigDecimal.ZERO;
        }
        
        return billDAO.getTotalRevenueByDateRange(startDate, endDate);
    }

    private String generateBillNumber() {
        String prefix = "BILL";
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + uuid;
    }

    private boolean validateBill(Bill bill) {
        if (bill == null) {
            return false;
        }
        
        if (bill.getAccountNo() == null || bill.getAccountNo().trim().isEmpty()) {
            return false;
        }
        
        if (bill.getUnits() < 0) {
            return false;
        }
        
        return true;
    }
}
