package com.pahanaEdu.dao;

import com.pahanaEdu.model.Bill;
import com.pahanaEdu.model.Customer;
import com.pahanaEdu.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public boolean insertBill(Bill bill) {
        String sql = "INSERT INTO bills (account_no, bill_date, units, amount) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, bill.getAccountNo());
            stmt.setTimestamp(2, bill.getBillDate());
            stmt.setInt(3, bill.getUnits());
            stmt.setBigDecimal(4, bill.getTotalAmount());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bill.setBillId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBill(Bill bill) {
        String sql = "UPDATE bills SET account_no=?, bill_date=?, units=?, amount=? WHERE bill_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bill.getAccountNo());
            stmt.setTimestamp(2, bill.getBillDate());
            stmt.setInt(3, bill.getUnits());
            stmt.setBigDecimal(4, bill.getTotalAmount());
            stmt.setInt(5, bill.getBillId());
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBill(int billId) {
        String sql = "DELETE FROM bills WHERE bill_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, billId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Bill getBillById(int billId) {
        String sql = "SELECT b.*, c.name, c.address, c.phone FROM bills b " +
                     "LEFT JOIN customers c ON b.account_no = c.account_no " +
                     "WHERE b.bill_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, billId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getString("account_no"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setUnits(rs.getInt("units"));
                    bill.setTotalAmount(rs.getBigDecimal("amount"));
                    
                    // Set customer information
                    Customer customer = new Customer();
                    customer.setAccountNumber(rs.getString("account_no"));
                    customer.setFullName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setPhone(rs.getString("phone"));
                    bill.setCustomer(customer);
                    
                    return bill;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT b.*, c.full_name, c.address, c.phone FROM bills b " +
                     "LEFT JOIN customers c ON b.account_no = c.account_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAccountNo(rs.getString("account_no"));
                bill.setBillDate(rs.getTimestamp("bill_date"));
                bill.setUnits(rs.getInt("units"));
                bill.setTotalAmount(rs.getBigDecimal("amount"));
                
                // Set customer information
                Customer customer = new Customer();
                customer.setAccountNumber(rs.getString("account_no"));
                customer.setFullName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhone(rs.getString("phone"));
                bill.setCustomer(customer);
                
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bills;
    }

    public List<Bill> getBillsByAccountNo(String accountNo) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT b.*, c.name, c.address, c.phone FROM bills b " +
                     "LEFT JOIN customers c ON b.account_no = c.account_no " +
                     "WHERE b.account_no=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getString("account_no"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setUnits(rs.getInt("units"));
                    bill.setTotalAmount(rs.getBigDecimal("amount"));
                    
                    // Set customer information
                    Customer customer = new Customer();
                    customer.setAccountNumber(rs.getString("account_no"));
                    customer.setFullName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setPhone(rs.getString("phone"));
                    bill.setCustomer(customer);
                    
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bills;
    }

    public List<Bill> getBillsByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT b.*, c.name, c.address, c.phone FROM bills b " +
                     "LEFT JOIN customers c ON b.account_no = c.account_no " +
                     "WHERE b.bill_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getString("account_no"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setUnits(rs.getInt("units"));
                    bill.setTotalAmount(rs.getBigDecimal("amount"));
                    
                    // Set customer information
                    Customer customer = new Customer();
                    customer.setAccountNumber(rs.getString("account_no"));
                    customer.setFullName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setPhone(rs.getString("phone"));
                    bill.setCustomer(customer);
                    
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bills;
    }

    public BigDecimal getTotalRevenueByDateRange(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT SUM(amount) as total_revenue FROM bills WHERE bill_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_revenue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}
