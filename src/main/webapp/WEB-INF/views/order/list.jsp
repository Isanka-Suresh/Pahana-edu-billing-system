<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders - Pahana Educational Billing System</title>
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
        .search-box {
            margin-bottom: 20px;
        }
        .action-buttons {
            white-space: nowrap;
        }
        .status-pending {
            background-color: #ffeeba;
        }
        .status-completed {
            background-color: #c3e6cb;
        }
        .status-cancelled {
            background-color: #f5c6cb;
        }
        .payment-pending {
            color: #856404;
        }
        .payment-paid {
            color: #155724;
        }
        .payment-refunded {
            color: #721c24;
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
                    <h1><i class="fas fa-shopping-cart"></i> Orders</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/orders/create" class="btn btn-sm btn-primary">
                            <i class="fas fa-plus-circle"></i> Create New Order
                        </a>
                    </div>
                </div>
                
                <!-- Search Box -->
                <div class="search-box">
                    <form action="${pageContext.request.contextPath}/orders/search" method="post" class="form">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <input type="text" name="searchTerm" class="form-control" placeholder="Search by customer name..." value="${searchTerm}">
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <select name="status" class="form-control">
                                        <option value="">-- Order Status --</option>
                                        <option value="pending" ${selectedStatus == 'pending' ? 'selected' : ''}>Pending</option>
                                        <option value="completed" ${selectedStatus == 'completed' ? 'selected' : ''}>Completed</option>
                                        <option value="cancelled" ${selectedStatus == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <select name="paymentStatus" class="form-control">
                                        <option value="">-- Payment Status --</option>
                                        <option value="pending" ${selectedPaymentStatus == 'pending' ? 'selected' : ''}>Pending</option>
                                        <option value="paid" ${selectedPaymentStatus == 'paid' ? 'selected' : ''}>Paid</option>
                                        <option value="refunded" ${selectedPaymentStatus == 'refunded' ? 'selected' : ''}>Refunded</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-primary btn-block">
                                    <i class="fas fa-search"></i> Search
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                
                <!-- Order Table -->
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Date</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th>Payment</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr class="status-${order.orderStatus}">
                                    <td>${order.orderId}</td>
                                    <td>${order.customer.fullName}</td>
                                    <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                    <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></td>
                                    <td>
                                        <span class="badge badge-${order.orderStatus}">
                                            ${order.orderStatus.substring(0, 1).toUpperCase()}${order.orderStatus.substring(1)}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge badge-${order.paymentStatus}">
                                            ${order.paymentStatus.substring(0, 1).toUpperCase()}${order.paymentStatus.substring(1)}
                                        </span>
                                    </td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/orders/view?orderId=${order.orderId}" class="btn btn-sm btn-info">
                                            <i class="fas fa-eye"></i> View
                                        </a>
                                        <c:if test="${order.orderStatus == 'pending'}">
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
                                        <a href="${pageContext.request.contextPath}/bills/generate?orderId=${order.orderId}" class="btn btn-sm btn-secondary">
                                            <i class="fas fa-file-invoice"></i> Bill
                                        </a>
                                        <a href="${pageContext.request.contextPath}/orders/delete?orderId=${order.orderId}" class="btn btn-sm btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this order? This action cannot be undone.')">
                                            <i class="fas fa-trash"></i> Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty orders}">
                                <tr>
                                    <td colspan="7" class="text-center">No orders found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
