package com.pahanaEdu.service;

import com.pahanaEdu.dao.CustomerDAO;
import com.pahanaEdu.model.Customer;

import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public boolean addCustomer(Customer customer) {
        if (!validateCustomer(customer)) {
            return false;
        }
        
        return customerDAO.insertCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        if (!validateCustomer(customer)) {
            return false;
        }
        
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        return customerDAO.deleteCustomer(accountNumber);
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }
        
        return customerDAO.getCustomerByAccountNumber(accountNumber);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public List<Customer> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllCustomers();
        }
        
        return customerDAO.searchCustomersByName(name);
    }

    public Customer getCustomer(int customerId) {
        if (customerId <= 0) {
            return null;
        }
        
        return customerDAO.getCustomerById(customerId);
    }

    private boolean validateCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }
        
        if (customer.getAccountNumber() == null || customer.getAccountNumber().trim().isEmpty()) {
            return false;
        }
        
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
}
