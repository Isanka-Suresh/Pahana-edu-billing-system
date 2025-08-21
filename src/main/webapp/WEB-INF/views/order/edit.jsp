<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Order - Pahana Educational Billing System</title>
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
        .form-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        .error-message {
            color: #dc3545;
            margin-bottom: 15px;
        }
        .order-summary {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
        }
        .order-items {
            margin-top: 20px;
        }
        .order-total {
            font-size: 1.2em;
            font-weight: bold;
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
                    <h1><i class="fas fa-edit"></i> Edit Order #${order.orderId}</h1>
                </div>
                
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger error-message">
                        ${errorMessage}
                    </div>
                </c:if>
                
                <div class="row">
                    <div class="col-md-6">
                        <!-- Add Item Form -->
                        <div class="form-container">
                            <h4><i class="fas fa-cart-plus"></i> Add Items to Order</h4>
                            <form action="${pageContext.request.contextPath}/orders/add-item" method="post">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <div class="form-group">
                                    <label for="itemId">Select Item <span class="text-danger">*</span></label>
                                    <select class="form-control" id="itemId" name="itemId" required>
                                        <option value="">-- Select Item --</option>
                                        <c:forEach var="item" items="${items}">
                                            <option value="${item.itemId}">${item.itemName} - Rs. ${item.unitPrice} (${item.stockQuantity} in stock)</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="quantity">Quantity <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" id="quantity" name="quantity" min="1" value="1" required>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add Item
                                </button>
                            </form>
                        </div>
                        
                        <!-- Order Form -->
                        <div class="form-container">
                            <h4><i class="fas fa-file-invoice"></i> Order Details</h4>
                            <form action="${pageContext.request.contextPath}/orders/update" method="post">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <div class="form-group">
                                    <label for="customerId">Customer <span class="text-danger">*</span></label>
                                    <select class="form-control" id="customerId" name="customerId" required>
                                        <option value="">-- Select Customer --</option>
                                        <c:forEach var="customer" items="${customers}">
                                            <option value="${customer.customerId}" ${order.customerId == customer.customerId ? 'selected' : ''}>
                                                ${customer.fullName} (${customer.accountNumber})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="status">Order Status</label>
                                    <select class="form-control" id="status" name="status">
                                        <option value="pending" ${order.status == 'pending' ? 'selected' : ''}>Pending</option>
                                        <option value="completed" ${order.status == 'completed' ? 'selected' : ''}>Completed</option>
                                        <option value="cancelled" ${order.status == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="paymentStatus">Payment Status</label>
                                    <select class="form-control" id="paymentStatus" name="paymentStatus">
                                        <option value="pending" ${order.paymentStatus == 'pending' ? 'selected' : ''}>Pending</option>
                                        <option value="paid" ${order.paymentStatus == 'paid' ? 'selected' : ''}>Paid</option>
                                        <option value="refunded" ${order.paymentStatus == 'refunded' ? 'selected' : ''}>Refunded</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="notes">Notes</label>
                                    <textarea class="form-control" id="notes" name="notes" rows="3">${order.notes}</textarea>
                                </div>
                                <button type="submit" class="btn btn-success" ${empty order.orderItems ? 'disabled' : ''}>
                                    <i class="fas fa-save"></i> Update Order
                                </button>
                                <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                            </form>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <!-- Order Items -->
                        <div class="form-container">
                            <h4><i class="fas fa-list"></i> Order Items</h4>
                            <c:if test="${empty order.orderItems}">
                                <div class="alert alert-info">
                                    No items added to this order yet. Please add items using the form on the left.
                                </div>
                            </c:if>
                            <c:if test="${not empty order.orderItems}">
                                <div class="table-responsive order-items">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>Item</th>
                                                <th>Price</th>
                                                <th>Quantity</th>
                                                <th>Subtotal</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${order.orderItems}">
                                                <tr>
                                                    <td>${item.item.itemName}</td>
                                                    <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. "/></td>
                                                    <td>${item.quantity}</td>
                                                    <td><fmt:formatNumber value="${item.lineTotal}" type="currency" currencySymbol="Rs. "/></td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/orders/remove-item?itemId=${item.item.itemId}&orderId=${order.orderId}" class="btn btn-sm btn-danger">
                                                            <i class="fas fa-trash"></i> Remove
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                                <td colspan="3" class="text-right order-total">Total:</td>
                                                <td class="order-total"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></td>
                                                <td></td>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div>
                            </c:if>
                        </div>
                        
                        <!-- Order Information -->
                        <div class="form-container">
                            <h4><i class="fas fa-info-circle"></i> Order Information</h4>
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Order ID:</strong> ${order.orderId}</p>
                                    <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Current Status:</strong> 
                                        <span class="badge badge-${order.status}">
                                            ${order.status.substring(0, 1).toUpperCase()}${order.status.substring(1)}
                                        </span>
                                    </p>
                                    <p><strong>Payment Status:</strong> 
                                        <span class="badge badge-${order.paymentStatus}">
                                            ${order.paymentStatus.substring(0, 1).toUpperCase()}${order.paymentStatus.substring(1)}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
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
