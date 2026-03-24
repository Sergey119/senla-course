package com.example.task.controller;

import com.example.task.dto.OrderDto;
import com.example.task.exception.ServiceException;
import com.example.task.model.OrderStatus;
import com.example.task.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws ServiceException {
        OrderDto created = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Integer id) throws ServiceException {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable("status") String status) throws ServiceException {
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid status value. Allowed values: PENDING, IN_PROGRESS, COMPLETED, CANCELLED");
        }
        List<OrderDto> orders = orderService.findByStatus(orderStatus);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderDto>> searchOrders(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("status") String status) throws ServiceException {
        try {
            LocalDateTime start = parseDate(startDate);
            LocalDateTime end = parseDate(endDate);
            OrderStatus orderStatus;
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ServiceException("Invalid status value. Allowed values: PENDING, IN_PROGRESS, COMPLETED, CANCELLED");
            }
            List<OrderDto> orders = orderService.findOrdersByDateRange(start, end, orderStatus);
            return ResponseEntity.ok(orders);
        } catch (DateTimeParseException e) {
            throw new ServiceException("Invalid date format. Use: yyyy-MM-ddTHH:mm:ss or yyyy-MM-dd HH:mm:ss");
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") String status) throws ServiceException {
        orderService.findById(id);
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid status value. Allowed values: PENDING, IN_PROGRESS, COMPLETED, CANCELLED");
        }
        orderService.updateOrderStatus(id, orderStatus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Integer id) throws ServiceException {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            throw e;
        }
    }

    private LocalDateTime parseDate(String dateStr) {
        String normalizedDate = dateStr.replace("T", " ");

        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(normalizedDate, formatter);
            } catch (DateTimeParseException e2) {
                throw new DateTimeParseException("Cannot parse date: " + dateStr, dateStr, 0);
            }
        }
    }
}