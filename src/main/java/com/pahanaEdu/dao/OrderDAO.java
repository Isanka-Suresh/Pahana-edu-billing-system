package com.pahanaEdu.dao;

import com.pahanaEdu.model.Customer;
import com.pahanaEdu.model.Order;
import com.pahanaEdu.model.OrderItem;
import com.pahanaEdu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    
    private OrderItemDAO orderItemDAO = new OrderItemDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public boolean insertOrder(Order order) {
        String sql = "INSERT INTO orders (order_number, customer_id, order_date, total_amount, order_status, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, order.getOrderNumber());
                stmt.setInt(2, order.getCustomerId());
                stmt.setTimestamp(3, order.getOrderDate());
                stmt.setDouble(4, order.getTotalAmount());
                stmt.setString(5, order.getOrderStatus());
                stmt.setString(6, order.getPaymentStatus());
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int orderId = generatedKeys.getInt(1);
                            order.setOrderId(orderId);
                            
                            // Insert order items
                            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                                for (OrderItem item : order.getOrderItems()) {
                                    item.setOrderId(orderId);
                                    if (!orderItemDAO.insertOrderItem(item, conn)) {
                                        conn.rollback();
                                        return false;
                                    }
                                }
                            }
                            
                            conn.commit();
                            return true;
                        }
                    }
                }
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET customer_id=?, order_date=?, total_amount=?, order_status=?, payment_status=? " +
                     "WHERE order_id=?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, order.getCustomerId());
                stmt.setTimestamp(2, order.getOrderDate());
                stmt.setDouble(3, order.getTotalAmount());
                stmt.setString(4, order.getOrderStatus());
                stmt.setString(5, order.getPaymentStatus());
                stmt.setInt(6, order.getOrderId());
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows > 0) {
                    // Delete existing order items
                    orderItemDAO.deleteOrderItemsByOrderId(order.getOrderId(), conn);
                    
                    // Insert updated order items
                    if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                        for (OrderItem item : order.getOrderItems()) {
                            item.setOrderId(order.getOrderId());
                            if (!orderItemDAO.insertOrderItem(item, conn)) {
                                conn.rollback();
                                return false;
                            }
                        }
                    }
                    
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteOrder(int orderId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Delete order items first
            if (orderItemDAO.deleteOrderItemsByOrderId(orderId, conn)) {
                // Then delete the order
                String sql = "DELETE FROM orders WHERE order_id=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, orderId);
                    int affectedRows = stmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        conn.commit();
                        return true;
                    }
                }
            }
            
            conn.rollback();
            return false;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, c.account_no, c.name, c.address, c.phone " +
                     "FROM orders o " +
                     "LEFT JOIN customers c ON o.customer_id = c.id " +
                     "WHERE o.order_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    
                    // Set customer information
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setFullName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setPhone(rs.getString("phone"));
                    order.setCustomer(customer);
                    
                    // Get order items
                    List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
                    order.setOrderItems(orderItems);
                    
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.account_number, c.full_name, c.address, c.phone " +
                     "FROM orders o " +
                     "LEFT JOIN customers c ON o.customer_id = c.customer_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderNumber(rs.getString("order_number"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setPaymentStatus(rs.getString("payment_status"));
                
                // Set customer information
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setAccountNumber(rs.getString("account_no"));
                customer.setFullName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhone(rs.getString("phone"));
                order.setCustomer(customer);
                
                // Get order items
                List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
                order.setOrderItems(orderItems);
                
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.* FROM orders o WHERE o.customer_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    
                    // Get customer information
                    Customer customer = customerDAO.getCustomerByAccountNumber(String.valueOf(customerId));
                    order.setCustomer(customer);
                    
                    // Get order items
                    List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
                    order.setOrderItems(orderItems);
                    
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status=? WHERE order_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePaymentStatus(int orderId, String status) {
        String sql = "UPDATE orders SET payment_status=? WHERE order_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.account_no, c.name, c.address, c.phone " +
                    "FROM orders o " +
                    "LEFT JOIN customers c ON o.customer_id = c.id " +
                    "WHERE o.order_status=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    
                    // Set customer information
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setFullName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setPhone(rs.getString("phone"));
                    order.setCustomer(customer);
                    
                    // Get order items
                    List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
                    order.setOrderItems(orderItems);
                    
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }

    public String generateOrderNumber() {
        String prefix = "ORD";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
}
