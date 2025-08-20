<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Bill - Pahana Educational Billing System</title>
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
        .invoice-container {
            background-color: #fff;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }
        .invoice-header {
            border-bottom: 1px solid #dee2e6;
            padding-bottom: 20px;
            margin-bottom: 20px;
        }
        .invoice-title {
            font-size: 24px;
            font-weight: bold;
            color: #343a40;
        }
        .company-details {
            margin-bottom: 20px;
        }
        .company-name {
            font-size: 18px;
            font-weight: bold;
            color: #343a40;
        }
        .invoice-details {
            margin-bottom: 20px;
        }
        .invoice-details-title {
            font-weight: bold;
            color: #6c757d;
        }
        .customer-details {
            margin-bottom: 20px;
        }
        .table-items th {
            background-color: #f8f9fa;
        }
        .table-total {
            margin-top: 20px;
            width: 300px;
            margin-left: auto;
        }
        .table-total td {
            border-top: none;
        }
        .table-total .total-row {
            border-top: 2px solid #dee2e6;
            font-weight: bold;
        }
        .invoice-footer {
            margin-top: 30px;
            border-top: 1px solid #dee2e6;
            padding-top: 20px;
        }
        .payment-info {
            margin-top: 20px;
        }
        .action-buttons {
            margin-top: 20px;
        }
        .badge-pending {
            background-color: #ffc107;
            color: #212529;
        }
        .badge-paid {
            background-color: #28a745;
        }
        .badge-refunded {
            background-color: #dc3545;
        }
        @media print {
            .no-print {
                display: none;
            }
            body {
                padding-top: 0;
                background-color: #fff;
            }
            .main-content {
                margin-left: 0;
                padding: 0;
            }
            .invoice-container {
                box-shadow: none;
                padding: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Bar - Hidden when printing -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top no-print">
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

    <!-- Sidebar - Hidden when printing -->
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-none d-md-block sidebar no-print">
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
                            <a class="nav-link active" href="${pageContext.request.contextPath}/bills">
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
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom no-print">
                    <h1><i class="fas fa-file-invoice-dollar"></i> View Bill</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group mr-2">
                            <a href="${pageContext.request.contextPath}/bills" class="btn btn-sm btn-outline-secondary">
                                <i class="fas fa-arrow-left"></i> Back to Bills
                            </a>
                        </div>
                    </div>
                </div>
                
                <!-- Invoice Container -->
                <div class="invoice-container">
                    <!-- Invoice Header -->
                    <div class="invoice-header">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="company-details">
                                    <div class="company-name">Pahana Educational Institute</div>
                                    <div>123 Education Street, Colombo</div>
                                    <div>Sri Lanka</div>
                                    <div>Tel: +94 11 2345678</div>
                                    <div>Email: info@pahanaedu.lk</div>
                                </div>
                            </div>
                            <div class="col-md-6 text-right">
                                <div class="invoice-title">INVOICE</div>
                                <div class="invoice-details">
                                    <div><span class="invoice-details-title">Invoice Number:</span> INV-${order.id}</div>
                                    <div><span class="invoice-details-title">Date:</span> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd" /></div>
                                    <div>
                                        <span class="invoice-details-title">Payment Status:</span>
                                        <span class="badge badge-${order.paymentStatus}">
                                            ${order.paymentStatus.substring(0, 1).toUpperCase()}${order.paymentStatus.substring(1)}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Customer Details -->
                    <div class="customer-details">
                        <div class="row">
                            <div class="col-md-6">
                                <h5>Bill To:</h5>
                                <div><strong>${customer.name}</strong></div>
                                <div>${customer.address}</div>
                                <div>Tel: ${customer.phone}</div>
                                <div>Email: ${customer.email}</div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Order Items -->
                    <div class="table-responsive">
                        <table class="table table-items">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Item</th>
                                    <th>Description</th>
                                    <th class="text-right">Unit Price</th>
                                    <th class="text-right">Quantity</th>
                                    <th class="text-right">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="orderItem" items="${orderItems}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${orderItem.itemName}</td>
                                        <td>${orderItem.itemDescription}</td>
                                        <td class="text-right"><fmt:formatNumber value="${orderItem.unitPrice}" type="currency" currencySymbol="Rs. "/></td>
                                        <td class="text-right">${orderItem.quantity}</td>
                                        <td class="text-right"><fmt:formatNumber value="${orderItem.unitPrice * orderItem.quantity}" type="currency" currencySymbol="Rs. "/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    
                    <!-- Totals -->
                    <div class="row">
                        <div class="col-md-6">
                            <div class="payment-info">
                                <h5>Payment Information:</h5>
                                <p>Please make payment within 30 days of invoice date.</p>
                                <p><strong>Bank Details:</strong><br>
                                Bank: Bank of Ceylon<br>
                                Account Name: Pahana Educational Institute<br>
                                Account Number: 1234567890<br>
                                Branch: Colombo Main</p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <table class="table table-total">
                                <tr>
                                    <td>Subtotal:</td>
                                    <td class="text-right"><fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="Rs. "/></td>
                                </tr>
                                <tr>
                                    <td>Tax (${order.taxRate}%):</td>
                                    <td class="text-right"><fmt:formatNumber value="${order.taxAmount}" type="currency" currencySymbol="Rs. "/></td>
                                </tr>
                                <c:if test="${order.discount > 0}">
                                    <tr>
                                        <td>Discount:</td>
                                        <td class="text-right"><fmt:formatNumber value="${order.discount}" type="currency" currencySymbol="Rs. "/></td>
                                    </tr>
                                </c:if>
                                <tr class="total-row">
                                    <td><strong>Total:</strong></td>
                                    <td class="text-right"><strong><fmt:formatNumber value="${order.total}" type="currency" currencySymbol="Rs. "/></strong></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    
                    <!-- Notes -->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="invoice-footer">
                                <h5>Notes:</h5>
                                <p>${order.notes != null ? order.notes : 'No notes available.'}</p>
                                <p class="text-center mt-4">Thank you for your business!</p>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Action Buttons - Hidden when printing -->
                    <div class="action-buttons text-center no-print">
                        <button onclick="window.print();" class="btn btn-primary">
                            <i class="fas fa-print"></i> Print Invoice
                        </button>
                        <a href="${pageContext.request.contextPath}/bills/generate?orderId=${order.id}" class="btn btn-success">
                            <i class="fas fa-file-pdf"></i> Download PDF
                        </a>
                        <a href="${pageContext.request.contextPath}/orders/edit?orderId=${order.id}" class="btn btn-secondary">
                            <i class="fas fa-edit"></i> Edit Order
                        </a>
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
