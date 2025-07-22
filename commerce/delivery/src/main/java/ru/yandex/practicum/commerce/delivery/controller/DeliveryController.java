package ru.yandex.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.validation.ValidUUID;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/delivery")
@Validated
public class DeliveryController {

    private final DeliveryService service;

    @PutMapping
    public ResponseEntity<DeliveryDto> planDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.info("\nDeliveryController.planDelivery: accepted {}", deliveryDto);
        return ResponseEntity.ok(service.planDelivery(deliveryDto));
    }

    @PostMapping("/successful")
    public ResponseEntity<Void> makeDeliverySuccessful(@RequestBody @ValidUUID String deliveryId) {
        log.info("\nDeliveryController.makeDeliverySuccessful: accepted {}", deliveryId);
        service.makeDeliverySuccessful(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/picked")
    public ResponseEntity<Void> makeDeliveryPicked(@RequestBody @ValidUUID String deliveryId) {
        log.info("\nDeliveryController.makeDeliveryPicked: accepted {}", deliveryId);
        service.makeDeliveryPicked(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/failed")
    public ResponseEntity<Void> makeDeliveryFailed(@RequestBody @ValidUUID String deliveryId) {
        log.info("\nDeliveryController.makeDeliveryFailed: accepted {}", deliveryId);
        service.makeDeliveryFailed(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cost")
    public ResponseEntity<BigDecimal> deliveryCost(@RequestBody @Valid OrderDto order) {
        log.info("\nDeliveryController.deliveryCost: accepted {}", order);
        return ResponseEntity.ok(service.deliveryCost(order));
    }
}
