package ru.yandex.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.validation.ValidUUID;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentDto> payment(@RequestBody @Valid OrderDto order) {
        log.info("\nPaymentController.payment: accepted {}", order);
        return ResponseEntity.ok(service.payment(order));
    }

    @PostMapping("/totalCost")
    public ResponseEntity<BigDecimal> getTotalCost(@RequestBody @Valid OrderDto order) {
        log.info("\nPaymentController.getTotalCost: accepted {}", order);
        return ResponseEntity.ok(service.getTotalCost(order));
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> orderPaid(@RequestBody @ValidUUID String orderId) {
        log.info("\nPaymentController.orderPaid: accepted {}", orderId);
        service.orderPaid(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/productCost")
    public ResponseEntity<BigDecimal> productCost(@RequestBody @Valid OrderDto order) {
        log.info("\nPaymentController.productCost: accepted {}", order);
        return ResponseEntity.ok(service.productCost(order));
    }

    @PostMapping("/failed")
    public ResponseEntity<Void> failedPayment(@RequestBody @Valid String orderId) {
        log.info("\nPaymentController.failedPayment: accepted {}", orderId);
        service.failedPayment(orderId);
        return ResponseEntity.ok().build();
    }

}
