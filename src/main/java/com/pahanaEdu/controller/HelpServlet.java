package com.pahanaEdu.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling help section
 */
@WebServlet("/help/*")
public class HelpServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Show main help page
            showHelpPage(request, response);
        } else if (pathInfo.equals("/customers")) {
            // Show customer help
            showCustomerHelp(request, response);
        } else if (pathInfo.equals("/items")) {
            // Show item help
            showItemHelp(request, response);
        } else if (pathInfo.equals("/orders")) {
            // Show order help
            showOrderHelp(request, response);
        } else if (pathInfo.equals("/bills")) {
            // Show bill help
            showBillHelp(request, response);
        } else {
            // Invalid path, redirect to main help page
            response.sendRedirect(request.getContextPath() + "/help");
        }
    }
    
    private void showHelpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activeSection", "main");
        request.getRequestDispatcher("/WEB-INF/views/help/help.jsp").forward(request, response);
    }
    
    private void showCustomerHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activeSection", "customers");
        request.getRequestDispatcher("/WEB-INF/views/help/help.jsp").forward(request, response);
    }
    
    private void showItemHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activeSection", "items");
        request.getRequestDispatcher("/WEB-INF/views/help/help.jsp").forward(request, response);
    }
    
    private void showOrderHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activeSection", "orders");
        request.getRequestDispatcher("/WEB-INF/views/help/help.jsp").forward(request, response);
    }
    
    private void showBillHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activeSection", "bills");
        request.getRequestDispatcher("/WEB-INF/views/help/help.jsp").forward(request, response);
    }
}
