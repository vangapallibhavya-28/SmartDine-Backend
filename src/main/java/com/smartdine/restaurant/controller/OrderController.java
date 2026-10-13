package com.smartdine.restaurant.controller;

import com.smartdine.restaurant.entity.Order;
import com.smartdine.restaurant.entity.OrderItem;
import com.smartdine.restaurant.service.OrderService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(
                    request.getCustomerName(),
                    request.getCustomerPhone(),
                    request.getOrderType(),
                    request.getOrderDateTime(),
                    request.getItems()
            );

            return ResponseEntity.ok(order);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get order items
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    orderService.getOrderItems(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Track an order
    @GetMapping("/{id}/tracking")
    public ResponseEntity<?> trackOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    orderService.getTrackingDetails(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Advance delivery status
    @PostMapping("/{id}/tracking/advance")
    public ResponseEntity<?> advanceTracking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    orderService.advanceTracking(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Request format for creating an order
    public static class CreateOrderRequest {
        private String customerName;
        private String customerPhone;
        private String orderType;
        private LocalDateTime orderDateTime;
        private List<OrderItem> items;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public LocalDateTime getOrderDateTime() {
            return orderDateTime;
        }

        public void setOrderDateTime(LocalDateTime orderDateTime) {
            this.orderDateTime = orderDateTime;
        }

        public List<OrderItem> getItems() {
            return items;
        }

        public void setItems(List<OrderItem> items) {
            this.items = items;
        }
    }
}

    
