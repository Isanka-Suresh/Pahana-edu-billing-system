package com.pahanaEdu.service;

import com.pahanaEdu.dao.ItemDAO;
import com.pahanaEdu.model.Item;

import java.util.List;

public class ItemService {
    private ItemDAO itemDAO;
    
    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    public boolean addItem(Item item) {
        if (!validateItem(item)) {
            return false;
        }
        
        return itemDAO.insertItem(item);
    }

    public boolean updateItem(Item item) {
        if (!validateItem(item)) {
            return false;
        }
        
        return itemDAO.updateItem(item);
    }

    public boolean deleteItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }
        
        return itemDAO.deleteItem(itemId);
    }

    public Item getItemById(int itemId) {
        if (itemId <= 0) {
            return null;
        }
        
        return itemDAO.getItemById(itemId);
    }

    public Item getItemByCode(String itemCode) {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            return null;
        }
        
        return itemDAO.getItemByCode(itemCode);
    }

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public List<Item> searchItemsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllItems();
        }
        
        return itemDAO.searchItemsByName(name);
    }

    public boolean updateStockQuantity(int itemId, int quantity) {
        if (itemId <= 0 || quantity < 0) {
            return false;
        }
        
        return itemDAO.updateStockQuantity(itemId, quantity);
    }
    
    private boolean validateItem(Item item) {
        if (item == null) {
            return false;
        }
        
        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            return false;
        }
        
        if (item.getUnitPrice() < 0) {
            return false;
        }
        
        if (item.getStockQuantity() < 0) {
            return false;
        }
        
        return true;
    }
}
