
package com.smartdine.restaurant.controller;

import com.smartdine.restaurant.entity.Order;
import com.smartdine.restaurant.entity.OrderItem;
import com.smartdine.restaurant.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create an order
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody Map<String, Object> request) {

        try {
            String customerName =
                    (String) request.get("customerName");

            String customerPhone =
                    (String) request.get("customerPhone");

            String orderType =
                    (String) request.get("orderType");

            LocalDateTime orderDateTime = null;
            Object dateTimeValue = request.get("orderDateTime");

            if (dateTimeValue != null
                    && !dateTimeValue.toString().isBlank()) {
                orderDateTime = LocalDateTime.parse(
                        dateTimeValue.toString());
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) request.get("items");

            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Your cart is empty."));
            }

            List<OrderItem> requestedItems = items.stream()
                    .map(item -> {
                        Object id = item.get("menuItemId");
                        Object quantity = item.get("quantity");

                        if (id == null || quantity == null) {
                            throw new IllegalArgumentException(
                                    "Each item needs menuItemId and quantity.");
                        }

                        OrderItem orderItem = new OrderItem();
                        orderItem.setMenuItemId(
                                Long.valueOf(id.toString()));
                        orderItem.setQuantity(
                                Integer.parseInt(quantity.toString()));

                        return orderItem;
                    })
                    .toList();

            Order savedOrder = orderService.createOrder(
                    customerName,
                    customerPhone,
                    orderType,
                    orderDateTime,
                    requestedItems);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Order placed successfully!",
                            "order", savedOrder
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Unable to place order."));
        }
    }

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get items belonging to one order
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getOrderItems(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    orderService.getOrderItems(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get delivery tracking details
    @GetMapping("/{id}/tracking")
    public ResponseEntity<?> getTracking(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    orderService.getTrackingDetails(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Advance delivery status for project demonstration
    @PostMapping("/{id}/tracking/advance")
    public ResponseEntity<?> advanceTracking(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    orderService.advanceTracking(id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}