<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - Pahana Educational Billing System</title>
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
            width: 200px;
            transition: all 0.3s;
        }
        @media (max-width: 767.98px) {
            .sidebar {
                margin-left: -200px;
            }
            .sidebar.active {
                margin-left: 0;
            }
            .main-content {
                margin-left: 0 !important;
                width: 100% !important;
            }
            .main-content.sidebar-active {
                margin-left: 220px !important;
                width: calc(100% - 220px) !important;
            }
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
            margin-left: 220px;
            padding: 20px;
            width: calc(100% - 220px);
        }
        .card {
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .order-header {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .order-details {
            margin-bottom: 20px;
        }
        .order-items {
            margin-top: 20px;
        }
        .order-total {
            font-size: 1.2em;
            font-weight: bold;
        }
        .badge-pending {
            background-color: #ffc107;
            color: #212529;
        }
        .badge-completed {
            background-color: #28a745;
        }
        .badge-cancelled {
            background-color: #dc3545;
        }
        .badge-paid {
            background-color: #28a745;
        }
        .badge-refunded {
            background-color: #dc3545;
        }
        .print-section {
            background-color: white;
            padding: 20px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .print-header {
            text-align: center;
            margin-bottom: 20px;
        }
        .print-header h2 {
            margin-bottom: 5px;
        }
        .print-header p {
            color: #6c757d;
        }
        .print-customer-details {
            margin-bottom: 20px;
        }
        .print-table {
            width: 100%;
            margin-bottom: 20px;
        }
        .print-table th, .print-table td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
        }
        .print-footer {
            margin-top: 30px;
            text-align: center;
            font-size: 0.9em;
            color: #6c757d;
        }
        @media print {
            body * {
                visibility: hidden;
            }
            .print-section, .print-section * {
                visibility: visible;
            }
            .print-section {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                border: none;
            }
            .no-print {
                display: none;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Bar - Hidden when printing -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top no-print">
        <button id="sidebarToggle" class="btn btn-dark d-md-none mr-2 no-print">
            <i class="fas fa-bars"></i>
        </button>
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
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="fas fa-user-circle"></i> Profile</a>
                        <div class="dropdown-divider"></div>
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
                            <a class="nav-link active" href="${pageContext.request.contextPath}/orders">
                                <i class="fas fa-shopping-cart"></i> Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/bills">
                                <i class="fas fa-file-invoice-dollar"></i> Bills
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/help">
                                <i class="fas fa-question-circle"></i> Help
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main role="main" class="main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                    <h1><i class="fas fa-eye"></i> Order Details #${order.orderId}</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group mr-2">
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="fas fa-print"></i> Print
                            </button>
                            <a href="${pageContext.request.contextPath}/bills/generate?orderId=${order.orderId}" class="btn btn-sm btn-outline-primary">
                                <i class="fas fa-file-pdf"></i> Generate PDF
                            </a>
                        </div>
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-sm btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Orders
                        </a>
                    </div>
                </div>
                
                <!-- Order Information -->
                <div class="card order-header no-print">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h5><i class="fas fa-info-circle"></i> Order Information</h5>
                                <p><strong>Order ID:</strong> ${order.orderId}</p>
                                <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                                <p><strong>Customer:</strong> ${order.customerName}</p>
                                <p><strong>Notes:</strong> ${not empty order.notes ? order.notes : 'N/A'}</p>
                            </div>
                            <div class="col-md-6">
                                <h5><i class="fas fa-tag"></i> Status</h5>
                                <p>
                                    <strong>Order Status:</strong> 
                                    <span class="badge badge-${order.status}">
                                        ${order.status.substring(0, 1).toUpperCase()}${order.status.substring(1)}
                                    </span>
                                </p>
                                <p>
                                    <strong>Payment Status:</strong> 
                                    <span class="badge badge-${order.paymentStatus}">
                                        ${order.paymentStatus.substring(0, 1).toUpperCase()}${order.paymentStatus.substring(1)}
                                    </span>
                                </p>
                                <div class="mt-3">
                                    <c:if test="${order.status == 'pending'}">
                                        <a href="${pageContext.request.contextPath}/orders/edit?orderId=${order.orderId}" class="btn btn-sm btn-primary">
                                            <i class="fas fa-edit"></i> Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/orders/complete?orderId=${order.orderId}" class="btn btn-sm btn-success"
                                           onclick="return confirm('Are you sure you want to mark this order as completed?')">
                                            <i class="fas fa-check"></i> Complete
                                        </a>
                                        <a href="${pageContext.request.contextPath}/orders/cancel?orderId=${order.orderId}" class="btn btn-sm btn-warning"
                                           onclick="return confirm('Are you sure you want to cancel this order?')">
                                            <i class="fas fa-ban"></i> Cancel
                                        </a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/orders/delete?orderId=${order.orderId}" class="btn btn-sm btn-danger"
                                       onclick="return confirm('Are you sure you want to delete this order? This action cannot be undone.')">
                                        <i class="fas fa-trash"></i> Delete
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Order Items -->
                <div class="card no-print">
                    <div class="card-header">
                        <h5><i class="fas fa-list"></i> Order Items</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Item</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${order.orderItems}" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${item.item.itemName}</td>
                                            <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. "/></td>
                                            <td>${item.quantity}</td>
                                            <td><fmt:formatNumber value="${item.lineTotal}" type="currency" currencySymbol="Rs. "/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="4" class="text-right order-total">Total:</td>
                                        <td class="order-total"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                
                <!-- Printable Bill Section -->
                <div class="print-section">
                    <div class="print-header">
                        <h2>Pahana Educational Institute</h2>
                        <p>123 Education Street, Colombo, Sri Lanka</p>
                        <p>Tel: +94 11 2345678 | Email: info@pahanaedu.lk</p>
                        <hr>
                        <h3>INVOICE</h3>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 print-customer-details">
                            <p><strong>Bill To:</strong></p>
                            <p>${order.customerName}</p>
                            <p>Account #: ${order.customerAccountNumber}</p>
                            <p>${order.customerAddress}</p>
                            <p>Tel: ${order.customerPhone}</p>
                        </div>
                        <div class="col-md-6 text-right print-customer-details">
                            <p><strong>Invoice #:</strong> INV-${order.orderId}</p>
                            <p><strong>Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd" /></p>
                            <p><strong>Status:</strong> ${order.paymentStatus.substring(0, 1).toUpperCase()}${order.paymentStatus.substring(1)}</p>
                        </div>
                    </div>
                    
                    <table class="print-table">
                        <thead>
                            <tr>
                                <th style="text-align: left;">#</th>
                                <th style="text-align: left;">Item Description</th>
                                <th style="text-align: right;">Price</th>
                                <th style="text-align: right;">Quantity</th>
                                <th style="text-align: right;">Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.orderItems}" varStatus="status">
                                <tr>
                                    <td style="text-align: left;">${status.index + 1}</td>
                                    <td style="text-align: left;">${item.item.itemName}</td>
                                    <td style="text-align: right;"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. "/></td>
                                    <td style="text-align: right;">${item.quantity}</td>
                                    <td style="text-align: right;"><fmt:formatNumber value="${item.lineTotal}" type="currency" currencySymbol="Rs. "/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="4" style="text-align: right;"><strong>Subtotal:</strong></td>
                                <td style="text-align: right;"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></td>
                            </tr>
                            <tr>
                                <td colspan="4" style="text-align: right;"><strong>Tax (0%):</strong></td>
                                <td style="text-align: right;">Rs. 0.00</td>
                            </tr>
                            <tr>
                                <td colspan="4" style="text-align: right;"><strong>Total:</strong></td>
                                <td style="text-align: right;"><strong><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></strong></td>
                            </tr>
                        </tfoot>
                    </table>
                    
                    <div class="row">
                        <div class="col-md-8">
                            <p><strong>Payment Terms:</strong> Due on receipt</p>
                            <p><strong>Notes:</strong> ${not empty order.notes ? order.notes : 'Thank you for your business!'}</p>
                        </div>
                    </div>
                    
                    <div class="print-footer">
                        <p>This is a computer-generated invoice and does not require a signature.</p>
                        <p>Pahana Educational Institute | www.pahanaedu.lk</p>
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#sidebarToggle').on('click', function() {
                $('.sidebar').toggleClass('active');
                $('.main-content').toggleClass('sidebar-active');
            });
        });
    </script>
</body>
</html>
