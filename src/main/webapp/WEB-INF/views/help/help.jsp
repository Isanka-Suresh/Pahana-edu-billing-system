<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help - Pahana Educational Billing System</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 56px;
        }
        .sidebar {
            position: fixed;
            top: 56px;
            bottom: 0;
            left: 0;
            z-index: 100;
            padding: 48px 0 0;
            box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
            background-color: #343a40;
        }
        .sidebar-sticky {
            position: relative;
            top: 0;
            height: calc(100vh - 48px);
            padding-top: .5rem;
            overflow-x: hidden;
            overflow-y: auto;
        }
        .sidebar .nav-link {
            font-weight: 500;
            color: #fff;
            padding: 0.75rem 1rem;
        }
        .sidebar .nav-link:hover {
            color: #007bff;
        }
        .sidebar .nav-link.active {
            color: #007bff;
        }
        .sidebar .nav-link i {
            margin-right: 10px;
        }
        .main-content {
            margin-left: 240px;
            padding: 20px;
        }
        .help-card {
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .help-nav {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .help-nav .nav-link {
            color: #495057;
            border-radius: 0;
            padding: 8px 16px;
        }
        .help-nav .nav-link.active {
            background-color: #007bff;
            color: #fff;
        }
        .help-content {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .help-section {
            margin-bottom: 30px;
        }
        .help-section h3 {
            border-bottom: 1px solid #dee2e6;
            padding-bottom: 10px;
            margin-bottom: 15px;
        }
        .faq-item {
            margin-bottom: 20px;
        }
        .faq-question {
            font-weight: bold;
            color: #007bff;
            margin-bottom: 5px;
        }
        .faq-answer {
            padding-left: 15px;
        }
        .step-by-step {
            margin-left: 20px;
            margin-bottom: 15px;
        }
        .step-by-step li {
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">Pahana Educational Billing System</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-user"></i> ${sessionScope.username}
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
                    </div>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Sidebar -->
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-none d-md-block sidebar">
                <div class="sidebar-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/customers">
                                <i class="fas fa-users"></i> Customers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/items">
                                <i class="fas fa-box"></i> Items
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/orders">
                                <i class="fas fa-shopping-cart"></i> Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/bills">
                                <i class="fas fa-file-invoice-dollar"></i> Bills
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/help">
                                <i class="fas fa-question-circle"></i> Help
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main role="main" class="main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h1><i class="fas fa-question-circle"></i> Help Center</h1>
                </div>
                
                <!-- Help Navigation -->
                <div class="help-nav">
                    <ul class="nav nav-pills">
                        <li class="nav-item">
                            <a class="nav-link ${activeSection == 'main' || empty activeSection ? 'active' : ''}" href="${pageContext.request.contextPath}/help">
                                <i class="fas fa-home"></i> General
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${activeSection == 'customers' ? 'active' : ''}" href="${pageContext.request.contextPath}/help/customers">
                                <i class="fas fa-users"></i> Customers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${activeSection == 'items' ? 'active' : ''}" href="${pageContext.request.contextPath}/help/items">
                                <i class="fas fa-box"></i> Items
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${activeSection == 'orders' ? 'active' : ''}" href="${pageContext.request.contextPath}/help/orders">
                                <i class="fas fa-shopping-cart"></i> Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${activeSection == 'bills' ? 'active' : ''}" href="${pageContext.request.contextPath}/help/bills">
                                <i class="fas fa-file-invoice-dollar"></i> Bills
                            </a>
                        </li>
                    </ul>
                </div>
                
                <!-- Help Content -->
                <div class="help-content">
                    <!-- General Help Section -->
                    <c:if test="${activeSection == 'main' || empty activeSection}">
                        <div class="help-section">
                            <h3><i class="fas fa-info-circle"></i> About Pahana Educational Billing System</h3>
                            <p>
                                Pahana Educational Billing System is a comprehensive solution designed to manage customer accounts, 
                                inventory, orders, and billing for educational institutions. This system helps streamline administrative 
                                tasks and provides an efficient way to manage financial transactions.
                            </p>
                            <p>
                                The system includes modules for customer management, item inventory, order processing, and bill generation. 
                                It is designed to be user-friendly and intuitive, allowing staff to quickly learn and use the system effectively.
                            </p>
                        </div>
                        
                        <div class="help-section">
                            <h3><i class="fas fa-question-circle"></i> Frequently Asked Questions</h3>
                            
                            <div class="faq-item">
                                <div class="faq-question">How do I log in to the system?</div>
                                <div class="faq-answer">
                                    To log in, navigate to the login page and enter your username and password. 
                                    If you don't have login credentials, please contact your system administrator.
                                </div>
                            </div>
                            
                            
                            
                            
                            
                            <div class="faq-item">
                                <div class="faq-question">How do I navigate the system?</div>
                                <div class="faq-answer">
                                    The system has a sidebar navigation menu on the left side of the screen. 
                                    Click on the menu items to access different sections of the system. 
                                    The dashboard provides an overview of key metrics and quick access to common tasks.
                                </div>
                            </div>
                        </div>
                        
                        <div class="help-section">
                            <h3><i class="fas fa-phone-alt"></i> Contact Support</h3>
                            <p>
                                If you need additional help or have questions not covered in this help section, 
                                please contact our support team:
                            </p>
                            <ul>
                                <li><strong>Email:</strong> support@pahanaedu.lk</li>
                                <li><strong>Phone:</strong> +94 11 2345678</li>
                                <li><strong>Hours:</strong> Monday to Friday, 8:30 AM to 5:00 PM</li>
                            </ul>
                        </div>
                    </c:if>
                    
                    <!-- Customer Help Section -->
                    <c:if test="${activeSection == 'customers'}">
                        <div class="help-section">
                            <h3><i class="fas fa-users"></i> Customer Management</h3>
                            <p>
                                The Customer Management module allows you to add, edit, and manage customer information. 
                                You can keep track of customer details, view their order history, and generate bills for them.
                            </p>
                            
                            <h4>Adding a New Customer</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Customers section by clicking on "Customers" in the sidebar.</li>
                                <li>Click on the "Add New Customer" button.</li>
                                <li>Fill in the required information (fields marked with * are mandatory).</li>
                                <li>Click "Save" to add the customer to the system.</li>
                            </ol>
                            
                            <h4>Editing Customer Information</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Customers section.</li>
                                <li>Find the customer you want to edit in the list.</li>
                                <li>Click on the "Edit" button (pencil icon) next to the customer's name.</li>
                                <li>Update the customer information as needed.</li>
                                <li>Click "Update" to save the changes.</li>
                            </ol>
                            
                            <h4>Searching for Customers</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Customers section.</li>
                                <li>Use the search box at the top of the customer list.</li>
                                <li>Enter the customer's name, account number, or other identifying information.</li>
                                <li>Press Enter or click the search button to find matching customers.</li>
                            </ol>
                            
                            <h4>Deleting a Customer</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Customers section.</li>
                                <li>Find the customer you want to delete in the list.</li>
                                <li>Click on the "Delete" button (trash icon) next to the customer's name.</li>
                                <li>Confirm the deletion when prompted.</li>
                            </ol>
                            <p class="text-danger">
                                <strong>Note:</strong> Deleting a customer will remove all their information from the system. 
                                This action cannot be undone. If the customer has associated orders or bills, 
                                you may not be able to delete them.
                            </p>
                        </div>
                    </c:if>
                    
                    <!-- Item Help Section -->
                    <c:if test="${activeSection == 'items'}">
                        <div class="help-section">
                            <h3><i class="fas fa-box"></i> Item Management</h3>
                            <p>
                                The Item Management module allows you to manage your inventory of products or services. 
                                You can add new items, update existing ones, track stock levels, and manage pricing.
                            </p>
                            
                            <h4>Adding a New Item</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Items section by clicking on "Items" in the sidebar.</li>
                                <li>Click on the "Add New Item" button.</li>
                                <li>Fill in the required information (fields marked with * are mandatory).</li>
                                <li>Click "Save" to add the item to the inventory.</li>
                            </ol>
                            
                            <h4>Editing Item Information</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Items section.</li>
                                <li>Find the item you want to edit in the list.</li>
                                <li>Click on the "Edit" button (pencil icon) next to the item's name.</li>
                                <li>Update the item information as needed.</li>
                                <li>Click "Update" to save the changes.</li>
                            </ol>
                            
                            <h4>Managing Stock Levels</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Items section.</li>
                                <li>Find the item you want to update in the list.</li>
                                <li>Click on the "Edit" button next to the item's name.</li>
                                <li>Update the "Quantity" field to reflect the current stock level.</li>
                                <li>Click "Update" to save the changes.</li>
                            </ol>
                            <p class="text-info">
                                <strong>Note:</strong> Items with low stock levels (below 10) will be highlighted in the item list 
                                to help you identify when it's time to restock.
                            </p>
                            
                            <h4>Searching for Items</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Items section.</li>
                                <li>Use the search box at the top of the item list.</li>
                                <li>Enter the item's name, code, or description.</li>
                                <li>Press Enter or click the search button to find matching items.</li>
                            </ol>
                            
                            <h4>Deleting an Item</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Items section.</li>
                                <li>Find the item you want to delete in the list.</li>
                                <li>Click on the "Delete" button (trash icon) next to the item's name.</li>
                                <li>Confirm the deletion when prompted.</li>
                            </ol>
                            <p class="text-danger">
                                <strong>Note:</strong> Deleting an item will remove it from the inventory. 
                                This action cannot be undone. If the item is associated with existing orders, 
                                you may not be able to delete it.
                            </p>
                        </div>
                    </c:if>
                    
                    <!-- Order Help Section -->
                    <c:if test="${activeSection == 'orders'}">
                        <div class="help-section">
                            <h3><i class="fas fa-shopping-cart"></i> Order Management</h3>
                            <p>
                                The Order Management module allows you to create and manage customer orders. 
                                You can add items to orders, track order status, and generate bills from orders.
                            </p>
                            
                            <h4>Creating a New Order</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Orders section by clicking on "Orders" in the sidebar.</li>
                                <li>Click on the "Create New Order" button.</li>
                                <li>Select a customer from the dropdown list.</li>
                                <li>Add items to the order using the "Add Items to Order" form:
                                    <ul>
                                        <li>Select an item from the dropdown list.</li>
                                        <li>Enter the quantity.</li>
                                        <li>Click "Add Item" to add it to the order.</li>
                                        <li>Repeat for additional items.</li>
                                    </ul>
                                </li>
                                <li>Add any notes or special instructions in the "Notes" field.</li>
                                <li>Click "Create Order" to finalize the order.</li>
                            </ol>
                            
                            <h4>Editing an Order</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Orders section.</li>
                                <li>Find the order you want to edit in the list.</li>
                                <li>Click on the "Edit" button next to the order.</li>
                                <li>Make the necessary changes to the order:
                                    <ul>
                                        <li>Add or remove items.</li>
                                        <li>Change quantities.</li>
                                        <li>Update customer information.</li>
                                        <li>Modify notes.</li>
                                    </ul>
                                </li>
                                <li>Click "Update Order" to save the changes.</li>
                            </ol>
                            <p class="text-warning">
                                <strong>Note:</strong> You can only edit orders that are in "Pending" status. 
                                Completed or cancelled orders cannot be modified.
                            </p>
                            
                            <h4>Managing Order Status</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Orders section.</li>
                                <li>Find the order you want to update in the list.</li>
                                <li>Use the action buttons to change the order status:
                                    <ul>
                                        <li>"Complete" button: Mark the order as completed.</li>
                                        <li>"Cancel" button: Mark the order as cancelled.</li>
                                    </ul>
                                </li>
                                <li>Confirm the status change when prompted.</li>
                            </ol>
                            
                            <h4>Generating a Bill from an Order</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Orders section.</li>
                                <li>Find the order you want to bill in the list.</li>
                                <li>Click on the "Bill" button next to the order.</li>
                                <li>The system will generate a bill and redirect you to the bill view.</li>
                                <li>From there, you can print the bill or download it as a PDF.</li>
                            </ol>
                        </div>
                    </c:if>
                    
                    <!-- Bill Help Section -->
                    <c:if test="${activeSection == 'bills'}">
                        <div class="help-section">
                            <h3><i class="fas fa-file-invoice-dollar"></i> Bill Management</h3>
                            <p>
                                The Bill Management module allows you to view, print, and manage bills generated from orders. 
                                You can track payment status and download bills as PDF documents.
                            </p>
                            
                            <h4>Viewing Bills</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Bills section by clicking on "Bills" in the sidebar.</li>
                                <li>You will see a list of all bills generated from completed orders.</li>
                                <li>Click on the "View" button next to a bill to see its details.</li>
                            </ol>
                            
                            <h4>Printing a Bill</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Bills section.</li>
                                <li>Find the bill you want to print in the list.</li>
                                <li>Click on the "View" button next to the bill.</li>
                                <li>In the bill view, click on the "Print" button.</li>
                                <li>Your browser's print dialog will open.</li>
                                <li>Configure your print settings as needed and click "Print".</li>
                            </ol>
                            
                            <h4>Downloading a Bill as PDF</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Bills section.</li>
                                <li>Find the bill you want to download in the list.</li>
                                <li>Click on the "Download PDF" button next to the bill.</li>
                                <li>The PDF will be generated and downloaded to your computer.</li>
                            </ol>
                            
                            <h4>Updating Payment Status</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Bills section.</li>
                                <li>Find the bill you want to update in the list.</li>
                                <li>Click on the "View" button next to the bill.</li>
                                <li>In the bill view, click on the "Edit" button.</li>
                                <li>Update the "Payment Status" field to reflect the current status (Pending, Paid, or Refunded).</li>
                                <li>Click "Update" to save the changes.</li>
                            </ol>
                            
                            <h4>Searching for Bills</h4>
                            <ol class="step-by-step">
                                <li>Navigate to the Bills section.</li>
                                <li>Use the search box at the top of the bill list.</li>
                                <li>Enter the customer's name, bill number, or other identifying information.</li>
                                <li>Use the payment status filter to narrow down results if needed.</li>
                                <li>Press Enter or click the search button to find matching bills.</li>
                            </ol>
                        </div>
                    </c:if>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
