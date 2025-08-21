package com.pahanaEdu.dao;

import com.pahanaEdu.model.Item;
import com.pahanaEdu.model.OrderItem;
import com.pahanaEdu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    
    private ItemDAO itemDAO = new ItemDAO();

    public boolean insertOrderItem(OrderItem orderItem) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return insertOrderItem(orderItem, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean insertOrderItem(OrderItem orderItem, Connection conn) {
        String sql = "INSERT INTO order_items (order_id, item_id, quantity, unit_price, line_total) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getItemId());
            stmt.setDouble(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getUnitPrice());
            stmt.setDouble(5, orderItem.getLineTotal());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderItem.setOrderItemId(generatedKeys.getInt(1));
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

    public boolean updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id=?, item_id=?, quantity=?, unit_price=?, line_total=? " +
                     "WHERE order_item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getItemId());
            stmt.setDouble(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getUnitPrice());
            stmt.setDouble(5, orderItem.getLineTotal());
            stmt.setInt(6, orderItem.getOrderItemId());
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM order_items WHERE order_item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItemId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderItemsByOrderId(int orderId, Connection conn) {
        String sql = "DELETE FROM order_items WHERE order_id=?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public OrderItem getOrderItemById(int orderItemId) {
        String sql = "SELECT oi.*, i.item_name, i.unit_price, i.description FROM order_items oi " +
                     "LEFT JOIN items i ON oi.item_id = i.item_id " +
                     "WHERE oi.order_item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderItemId(rs.getInt("order_item_id"));
                    orderItem.setOrderId(rs.getInt("order_id"));
                    orderItem.setItemId(rs.getInt("item_id"));
                    orderItem.setQuantity(rs.getDouble("quantity"));
                    orderItem.setUnitPrice(rs.getDouble("unit_price"));
                    orderItem.setLineTotal(rs.getDouble("line_total"));
                    
                    // Set item information
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDescription(rs.getString("description"));
                    orderItem.setItem(item);
                    
                    return orderItem;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, i.item_name, i.unit_price, i.description FROM order_items oi " +
                     "LEFT JOIN items i ON oi.item_id = i.item_id " +
                     "WHERE oi.order_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderItemId(rs.getInt("order_item_id"));
                    orderItem.setOrderId(rs.getInt("order_id"));
                    orderItem.setItemId(rs.getInt("item_id"));
                    orderItem.setQuantity(rs.getDouble("quantity"));
                    orderItem.setUnitPrice(rs.getDouble("unit_price"));
                    orderItem.setLineTotal(rs.getDouble("line_total"));
                    
                    // Set item information
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDescription(rs.getString("description"));
                    orderItem.setItem(item);
                    
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderItems;
}
}