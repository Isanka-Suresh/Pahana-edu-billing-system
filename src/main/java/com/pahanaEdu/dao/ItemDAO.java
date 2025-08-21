package com.pahanaEdu.dao;

import com.pahanaEdu.model.Item;
import com.pahanaEdu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public boolean insertItem(Item item) {
        String sql = "INSERT INTO items (item_code,item_name,description,unit_price,stock_quantity) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.setInt(5, item.getStockQuantity());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setItemId(generatedKeys.getInt(1));
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
    
    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_name=?,item_code=?,description=?, unit_price=?, stock_quantity=?  WHERE item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemCode());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.setInt(5, item.getStockQuantity());
            stmt.setInt(6, item.getItemId());
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Item getItemById(int itemId) {
        String sql = "SELECT * FROM items WHERE item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemCode(rs.getString("item_code"));
                    item.setItemName(rs.getString("item_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDescription(rs.getString("description"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public Item getItemByCode(String itemCode) {
        String sql = "SELECT * FROM items WHERE item_code=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemCode(rs.getString("item_code"));
                    item.setItemName(rs.getString("item_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDescription(rs.getString("description"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setItemCode(rs.getString("item_code"));
                item.setItemName(rs.getString("item_name"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setDescription(rs.getString("description"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    public List<Item> searchItemsByName(String name) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE item_name LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDescription(rs.getString("description"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    public boolean updateStockQuantity(int itemId, int quantity) {
        String sql = "UPDATE items SET stock_quantity=? WHERE item_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
