package com.pahanaEdu.controller;

import com.pahanaEdu.model.Item;
import com.pahanaEdu.service.ItemService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/items/*")
public class ItemServlet extends HttpServlet {
    private ItemService itemService;
    
    @Override
    public void init() {
        itemService = new ItemService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listItems(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteItem(request, response);
        } else {
            // Invalid path, redirect to item list
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Add new item
            addItem(request, response);
        } else if (pathInfo.equals("/update")) {
            // Update existing item
            updateItem(request, response);
        } else if (pathInfo.equals("/search")) {
            // Search items
            searchItems(request, response);
        } else {
            // Invalid path, redirect to item list
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
    
    private void listItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Item> items = itemService.getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/item/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        
        if (itemCode != null && !itemCode.trim().isEmpty()) {
            Item item = itemService.getItemByCode(itemCode);
            
            if (item != null) {
                request.setAttribute("item", item);
                request.getRequestDispatcher("/WEB-INF/views/item/edit.jsp").forward(request, response);
                return;
            }
        }
        
        // Item not found or invalid item code
        response.sendRedirect(request.getContextPath() + "/items");
    }
    
    private void addItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        String name = request.getParameter("itemName");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("unitPrice");
        String quantityStr = request.getParameter("stockQuantity");
        
        // Validate input
        if (itemCode == null || itemCode.trim().isEmpty() ||
            name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Item code and name are required");
            request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
            return;
        }
        
        // Parse price and quantity
        double price = 0.0;
        int quantity = 0;
        try {
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    request.setAttribute("errorMessage", "Price must be a non-negative number");
                    request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
                    return;
                }
            }
            
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    request.setAttribute("errorMessage", "Quantity must be a non-negative number");
                    request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Price and quantity must be valid numbers");
            request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
            return;
        }
        
        // Create item object
        Item item = new Item();
        item.setItemCode(itemCode);
        item.setItemName(name);
        item.setDescription(description != null ? description : "");
        item.setUnitPrice(price);
        item.setStockQuantity(quantity);
        
        // Add item
        if (itemService.addItem(item)) {
            // Success, redirect to item list
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            // Failed, show error message
            request.setAttribute("errorMessage", "Failed to add item. Item code may already exist.");
            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/views/item/add.jsp").forward(request, response);
        }
    }
    
    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        String name = request.getParameter("itemName");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("unitPrice");
        String quantityStr = request.getParameter("stockQuantity");
        String itemIdStr = request.getParameter("itemId");
        
        // Validate input
        if (itemCode == null || itemCode.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            itemIdStr == null || itemIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }
        
        // Parse item ID, price, and quantity
        int itemId;
        double price = 0.0;
        int quantity = 0;
        try {
            itemId = Integer.parseInt(itemIdStr);
            
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    request.setAttribute("errorMessage", "Price must be a non-negative number");
                    Item item = itemService.getItemByCode(itemCode);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("/WEB-INF/views/item/edit.jsp").forward(request, response);
                    return;
                }
            }
            
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    request.setAttribute("errorMessage", "Quantity must be a non-negative number");
                    Item item = itemService.getItemByCode(itemCode);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("/WEB-INF/views/item/edit.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }
        
        // Create item object
        Item item = new Item();
        item.setItemId(itemId);
        item.setItemCode(itemCode);
        item.setItemName(name);
        item.setDescription(description != null ? description : "");
        item.setUnitPrice(price);
        item.setStockQuantity(quantity);
        
        // Update item
        if (itemService.updateItem(item)) {
            // Success, redirect to item list
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            // Failed, show error message
            request.setAttribute("errorMessage", "Failed to update item");
            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/views/item/edit.jsp").forward(request, response);
        }
    }
    
    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");
        
        if (itemCode != null && !itemCode.trim().isEmpty()) {
            // Get the item by code first
            Item item = itemService.getItemByCode(itemCode);
            if (item != null) {
                // Then delete by ID
                itemService.deleteItem(item.getItemId());
            }
        }
        
        // Redirect to item list
        response.sendRedirect(request.getContextPath() + "/items");
    }
    
    private void searchItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        
        List<Item> items;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            items = itemService.searchItemsByName(searchTerm);
        } else {
            items = itemService.getAllItems();
        }
        
        request.setAttribute("items", items);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/WEB-INF/views/item/list.jsp").forward(request, response);
    }
}
