<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Order - Pahana Educational Billing System</title>
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
                    <h1><i class="fas fa-plus-circle"></i> Create New Order</h1>
                </div>
                
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger error-message">
                        ${errorMessage}
                    </div>
                </c:if>
                
                <!-- Customer Selection Form - Top Priority -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="form-container shadow-sm p-3 bg-white rounded">
                            <h4 class="text-primary mb-3"><i class="fas fa-user"></i> Select Customer</h4>
                            <div class="form-group">
                                <label for="customerId">Customer <span class="text-danger">*</span></label>
                                <select class="form-control custom-select" id="customerId" name="customerId" required>
                                    <option value="">-- Select Customer --</option>
                                    <c:forEach var="customer" items="${customers}">
                                        <option value="${customer.customerId}">
                                            ${customer.fullName} - (${customer.accountNumber})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <!-- Add Item Form -->
                        <div class="form-container shadow-sm p-3 mb-4 bg-white rounded">
                            <h4 class="text-primary mb-3"><i class="fas fa-cart-plus"></i> Add Items to Order</h4>
                            <form action="${pageContext.request.contextPath}/orders/add-item" method="post">
                                <input type="hidden" id="addItemCustomerId" name="customerId" value="${order.customerId}">
                                <div class="form-group">
                                    <label for="itemId">Select Item <span class="text-danger">*</span></label>
                                    <select class="form-control custom-select" id="itemId" name="itemId" required>
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
                                <button type="submit" class="btn btn-primary btn-block">
                                    <i class="fas fa-plus"></i> Add Item
                                </button>
                            </form>
                        </div>
                        
                        <!-- Order Form -->
                        <div class="form-container shadow-sm p-3 mb-4 bg-white rounded">
                            <h4 class="text-primary mb-3"><i class="fas fa-file-invoice"></i> Finalize Order</h4>
                            <form action="${pageContext.request.contextPath}/orders" method="post">
                                <input type="hidden" id="selectedCustomerId" name="customerId" value="${order.customerId}">
                                <div class="form-group">
                                    <label for="notes">Notes</label>
                                    <textarea class="form-control" id="notes" name="notes" rows="3" placeholder="Enter any special instructions or notes..."></textarea>
                                </div>
                                <button type="submit" class="btn btn-success btn-block" ${empty order.orderItems ? 'disabled' : ''}>
                                    <i class="fas fa-check"></i> Create Order
                                </button>
                                <c:if test="${empty order.orderItems}">
                                    <small class="form-text text-muted text-center mt-2">Please add at least one item to create an order</small>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                            </form>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <!-- Order Items -->
                        <div class="form-container shadow-sm p-3 mb-4 bg-white rounded">
                            <h4 class="text-primary mb-3"><i class="fas fa-list"></i> Order Items</h4>
                            <c:choose>
                                <c:when test="${empty order.orderItems}">
                                    <div class="alert alert-info text-center">
                                        <i class="fas fa-info-circle"></i> No items added to this order yet.
                                        <p class="mt-2 mb-0">Use the form on the left to add items.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead class="thead-light">
                                                <tr>
                                                    <th>Item</th>
                                                    <th>Price</th>
                                                    <th>Qty</th>
                                                    <th>Subtotal</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${order.orderItems}">
                                                    <tr>
                                                        <td><strong>${item.item.itemName}</strong></td>
                                                        <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. "/></td>
                                                        <td>${item.quantity}</td>
                                                        <td><fmt:formatNumber value="${item.lineTotal}" type="currency" currencySymbol="Rs. "/></td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/orders/remove-item?itemId=${item.item.itemId}" class="btn btn-sm btn-danger">
                                                                <i class="fas fa-trash"></i>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="3" class="text-right font-weight-bold">Total:</td>
                                                    <td class="font-weight-bold"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="Rs. "/></td>
                                                    <td></td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
    <script>
        // Sync customer selection between dropdown and hidden fields
        $(document).ready(function() {
            $('#customerId').change(function() {
                var selectedCustomerId = $(this).val();
                $('#selectedCustomerId').val(selectedCustomerId);
                $('#addItemCustomerId').val(selectedCustomerId);
                
                // Enable/disable create order button based on customer selection and items
                var hasItems = <c:out value="${not empty order.orderItems}" default="false"/>;
                var hasCustomer = selectedCustomerId !== '';
                
                var createOrderBtn = $('button[type="submit"]:contains("Create Order")');
                if (hasItems && hasCustomer) {
                    createOrderBtn.prop('disabled', false);
                    createOrderBtn.removeClass('btn-secondary').addClass('btn-success');
                } else {
                    createOrderBtn.prop('disabled', true);
                    createOrderBtn.removeClass('btn-success').addClass('btn-secondary');
                }
            });
            
            // Initialize on page load
            $('#customerId').trigger('change');
            
            // Set initial customer selection if order has customer
            var orderCustomerId = '${order.customerId}';
            if (orderCustomerId && orderCustomerId !== '0') {
                $('#customerId').val(orderCustomerId);
                $('#customerId').trigger('change');
            }
        });
    </script>
</body>
</html>
