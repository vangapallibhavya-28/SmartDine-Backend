
package com.smartdine.restaurant.service;

import com.smartdine.restaurant.entity.MenuItem;
import com.smartdine.restaurant.entity.Order;
import com.smartdine.restaurant.entity.OrderItem;
import com.smartdine.restaurant.repository.MenuItemRepository;
import com.smartdine.restaurant.repository.OrderItemRepository;
import com.smartdine.restaurant.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            MenuItemRepository menuItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public Order createOrder(
            String customerName,
            String customerPhone,
            String orderType,
            LocalDateTime orderDateTime,
            List<OrderItem> requestedItems) {

        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name is required.");
        }

        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer phone is required.");
        }

        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Your cart is empty.");
        }

        if (orderType == null
                || (!"NORMAL".equalsIgnoreCase(orderType)
                && !"PRE_ORDER".equalsIgnoreCase(orderType))) {
            throw new IllegalArgumentException(
                    "Order type must be NORMAL or PRE_ORDER.");
        }

        String normalizedOrderType =
                orderType.trim().toUpperCase(Locale.ROOT);

        LocalDateTime placementTime = LocalDateTime.now();

        LocalDateTime savedOrderTime;

        if ("PRE_ORDER".equals(normalizedOrderType)) {
            if (orderDateTime == null
                    || !orderDateTime.isAfter(placementTime)) {
                throw new IllegalArgumentException(
                        "Choose a future date and time for your pre-order.");
            }

            // Preserve the date and time selected by the customer.
            savedOrderTime = orderDateTime;
        } else {
            // Always record the actual placement time for normal orders.
            savedOrderTime = placementTime;
        }

        double subtotal = 0.0;

        // Validate items and calculate the subtotal.
        for (OrderItem requested : requestedItems) {

            if (requested == null || requested.getMenuItemId() == null) {
                throw new IllegalArgumentException(
                        "Menu item ID is required.");
            }

            int quantity = requested.getQuantity();

            if (quantity < 1 || quantity > 100) {
                throw new IllegalArgumentException(
                        "Quantity must be between 1 and 100.");
            }

            MenuItem menuItem = menuItemRepository
                    .findById(requested.getMenuItemId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Menu item not found: "
                                    + requested.getMenuItemId()));

            if (!menuItem.isAvailable()) {
                throw new IllegalArgumentException(
                        menuItem.getName() + " is unavailable.");
            }

            subtotal += menuItem.getPrice() * quantity;
        }

        double tax = Math.round(subtotal * 0.05 * 100.0) / 100.0;
        double total = Math.round((subtotal + tax) * 100.0) / 100.0;

        // Create a fresh order with independent tracking values.
        Order order = new Order();

        order.setCustomerName(customerName.trim());
        order.setCustomerPhone(customerPhone.trim());
        order.setOrderType(normalizedOrderType);
        order.setOrderDateTime(savedOrderTime);

        order.setStatus("PLACED");
        order.setDeliveryStatus("PLACED");
        order.setEstimatedDeliveryMinutes(30);

        // Do not reuse another order's tracking details.
        order.setDeliveryStartedAt(null);
        order.setDeliveryLatitude(null);
        order.setDeliveryLongitude(null);

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        // Save the individual items associated with this order.
        for (OrderItem requested : requestedItems) {

            MenuItem menuItem = menuItemRepository
                    .findById(requested.getMenuItemId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Menu item not found: "
                                    + requested.getMenuItemId()));

            OrderItem orderItem = new OrderItem();

            orderItem.setOrderId(savedOrder.getId());
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setItemName(menuItem.getName());
            orderItem.setPrice(menuItem.getPrice());
            orderItem.setQuantity(requested.getQuantity());
            orderItem.setItemTotal(
                    menuItem.getPrice() * requested.getQuantity());

            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Long orderId) {

        if (orderId == null || !orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException(
                    "Order not found: " + orderId);
        }

        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTrackingDetails(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found: " + orderId));

        Map<String, Object> tracking = new LinkedHashMap<>();

        tracking.put("orderId", order.getId());
        tracking.put("customerName", order.getCustomerName());
        tracking.put("orderDateTime", order.getOrderDateTime());
        tracking.put("deliveryStatus", order.getDeliveryStatus());
        tracking.put("estimatedDeliveryMinutes",
                order.getEstimatedDeliveryMinutes());
        tracking.put("deliveryStartedAt", order.getDeliveryStartedAt());
        tracking.put("deliveryLatitude", order.getDeliveryLatitude());
        tracking.put("deliveryLongitude", order.getDeliveryLongitude());

        return tracking;
    }

    @Transactional
    public Map<String, Object> advanceTracking(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found: " + orderId));

        String current = order.getDeliveryStatus();

        if (current == null || current.isBlank()) {
            current = "PLACED";
        }

        switch (current) {

            case "PLACED" ->
                    order.setDeliveryStatus("PREPARING");

            case "PREPARING" ->
                    order.setDeliveryStatus("READY");

            case "READY" -> {
                order.setDeliveryStatus("OUT_FOR_DELIVERY");
                order.setDeliveryStartedAt(LocalDateTime.now());
                order.setEstimatedDeliveryMinutes(30);
                order.setDeliveryLatitude(17.3850);
                order.setDeliveryLongitude(78.4867);
            }

            case "OUT_FOR_DELIVERY" -> {
                order.setDeliveryStatus("DELIVERED");
                order.setEstimatedDeliveryMinutes(0);
            }

            case "DELIVERED" ->
                    throw new IllegalArgumentException(
                            "This order has already been delivered.");

            default ->
                    throw new IllegalArgumentException(
                            "Unknown delivery status: " + current);
        }

        orderRepository.save(order);

        return getTrackingDetails(orderId);
    }
}