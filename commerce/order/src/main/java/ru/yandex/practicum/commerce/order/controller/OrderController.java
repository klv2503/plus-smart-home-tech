package ru.yandex.practicum.commerce.order.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.validation.ValidUUID;

import java.util.Collections;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService service;

    @CircuitBreaker(name = "order", fallbackMethod = "getDefaultUsersOrders")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUsersOrders(@RequestParam @NotBlank String userName) {
        log.info("\nOrderController.getUsersOrders: accepted username --{}--", userName);
        return ResponseEntity.ok(service.getUsersOrders(userName));
    }

    public ResponseEntity<List<OrderDto>> getDefaultUsersOrders(@RequestParam @NotBlank String userName, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
    }

    @PutMapping
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody @Valid CreateNewOrderRequest params) {
        log.info("\nOrderController.createNewOrder: accepted params {}", params);
        return ResponseEntity.ok(service.createNewOrder(params));
    }

    @PostMapping("/return")
    public ResponseEntity<OrderDto> returnOrder(@RequestBody @Valid ProductReturnRequest request) {
        log.info("\nOrderController.returnOrder: accepted request --{}--", request);
        return ResponseEntity.ok(service.returnOrder(request));
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDto> paymentSuccess(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.paymentSuccess: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.paymentSuccess(orderId));
    }

    @PostMapping("/failed")
    public ResponseEntity<OrderDto> paymentFailed(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.paymentFailed: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.paymentFailed(orderId));
    }

    @PostMapping("/delivery")
    public ResponseEntity<OrderDto> delivery(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.delivery: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.delivery(orderId));
    }

    @PostMapping("/delivery/failed")
    public ResponseEntity<OrderDto> deliveryFailed(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.deliveryFailed: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.deliveryFailed(orderId));
    }

    @PostMapping("/completed")
    public ResponseEntity<OrderDto> completeOrder(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.completeOrder: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.completeOrder(orderId));
    }

    @PostMapping("/calculate/total")
    public ResponseEntity<OrderDto> calculateTotal(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.calculateTotal: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.calculateTotal(orderId));
    }

    @PostMapping("/calculate/delivery")
    public ResponseEntity<OrderDto> calculateDelivery(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.calculateDelivery: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.calculateDelivery(orderId));
    }

    @PostMapping("/calculate/assembly")
    public ResponseEntity<OrderDto> assemblyOrder(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.assemblyOrder: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.assemblyOrder(orderId));
    }

    @PostMapping("/calculate/assembly/failed")
    public ResponseEntity<OrderDto> assemblyFailed(@RequestBody @ValidUUID String orderId) {
        log.info("\nOrderController.assemblyFailed: accepted orderId --{}--", orderId);
        return ResponseEntity.ok(service.assemblyFailed(orderId));
    }

}
